package app.Wallet.Service;

import app.Exception.DomainException;
import app.Transaction.Model.Transaction;
import app.Transaction.Model.TransactionStatus;
import app.Transaction.Model.TransactionType;
import app.Transaction.Service.TransactionService;
import app.User.Model.User;
import app.Wallet.Model.Wallet;
import app.Wallet.Model.WalletStatus;
import app.Wallet.Repository.WalletRepository;
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
