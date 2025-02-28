package app.Wallet.Service;

import app.User.Model.User;
import app.Wallet.Model.Wallet;
import app.Wallet.Model.WalletStatus;
import app.Wallet.Repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
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
