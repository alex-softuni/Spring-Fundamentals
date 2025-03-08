package app.wallet.Service;

import app.exception.DomainException;
import app.transaction.Model.Transaction;
import app.transaction.Model.TransactionStatus;
import app.transaction.Model.TransactionType;
import app.transaction.Service.TransactionService;
import app.user.Model.User;
import app.wallet.Model.Wallet;
import app.wallet.Model.WalletStatus;
import app.wallet.Repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Optional;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;


    @Autowired
    public WalletService(WalletRepository walletRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
    }

    @Transactional
    public Transaction topUp(UUID walletId, BigDecimal amount) {

        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);
        String transactionDescription = "Top up %.2f".formatted(amount.doubleValue());

        if (!optionalWallet.isPresent()) {
            throw new DomainException("Wallet does not exist");
        }
        Wallet wallet = optionalWallet.get();
        if (wallet.getStatus() == WalletStatus.INACTIVE) {
            return transactionService.createTransaction(wallet.getOwner(),
                    "EASY_MONEY_OPG",
                    walletId.toString(),
                    amount,
                    wallet.getBalance(),
                    wallet.getCurrency(),
                    TransactionType.DEPOSIT,
                    TransactionStatus.FAILED,
                    transactionDescription,
                    "Inactive wallet");
        }

        Transaction transaction = transactionService.createTransaction(wallet.getOwner(),
                "EASY_MONEY_OPG",
                walletId.toString(),
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCEEDED,
                transactionDescription,
                null);

        wallet.setBalance(wallet.getBalance().add(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);

        return transaction;
    }

    public void createNewWallet(User user) {

        Wallet wallet = initializeWallet(user);
        this.walletRepository.save(wallet);

        if (user.getWallets() == null) { // Defensive check
            user.setWallets(new ArrayList<>());
        }
        user.getWallets().add(wallet);

    }


    @Transactional
    public Transaction charge(User user, UUID walletId, BigDecimal amount, String transactionDescription) {

        Optional<Wallet> optionalWallet = walletRepository.findById(walletId);

        String failureReason = "";
        boolean isTransactionFailed = false;

        if (!optionalWallet.isPresent()) {
            isTransactionFailed = true;
            failureReason = "Wallet does not exist";
        }

        Wallet wallet = optionalWallet.get();

        if (wallet.getBalance().compareTo(amount) < 0) {
            isTransactionFailed = true;
            failureReason = "Insufficient funds!";
        }

        if (wallet.getStatus() == WalletStatus.INACTIVE) {
            isTransactionFailed = true;
            failureReason = "Wallet must be active";
        }

        if (isTransactionFailed) {
            return transactionService.createTransaction(
                    user,
                    walletId.toString(),
                    "SMART-WALLET-TEAM",
                    amount,
                    wallet.getBalance(),
                    wallet.getCurrency(),
                    TransactionType.WITHDRAWAL,
                    TransactionStatus.FAILED,
                    transactionDescription,
                    failureReason);
        }


        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedOn(LocalDateTime.now());
        walletRepository.save(wallet);

        return transactionService.createTransaction(
                user,
                walletId.toString(),
                "SMART-WALLET-TEAM",
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.WITHDRAWAL,
                TransactionStatus.SUCCEEDED,
                transactionDescription,
                null);
    }

    private static Wallet initializeWallet(User user) {
        return Wallet.builder()
                .owner(user)
                .balance(new BigDecimal("20.00"))
                .status(WalletStatus.ACTIVE)
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }
}
