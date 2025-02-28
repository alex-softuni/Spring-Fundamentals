package app;

import app.User.Model.Country;
import app.User.Service.UserService;
import app.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private final UserService userService;

    @Autowired
    public ConsoleRunner(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("stevie34")
                .password("stevie123")
                .country(Country.FRANCE)
                .build();
        userService.register(registerRequest);
    }
}
