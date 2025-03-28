package app;

import app.transaction.Service.TransactionService;
import app.user.Service.UserService;
import app.wallet.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class ConsoleRunner implements CommandLineRunner {
    private final UserService userService;
    private final TransactionService transactionService;
    private final WalletService walletService;

    @Autowired
    public ConsoleRunner(UserService userService, TransactionService transactionService, WalletService walletService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    @Override
    public void run(String... args) throws Exception {
//        RegisterRequest registerRequest = RegisterRequest.builder()
//                .username("stevie534")
//                .password("stevie123")
//                .country(Country.FRANCE)
//                .build();
//        userService.register(registerRequest);

     //   walletService.topUp(UUID.fromString("98c1ef71-8bb6-4336-a108-4e4f9266bba3"), new BigDecimal("20.00"));


    }
}
