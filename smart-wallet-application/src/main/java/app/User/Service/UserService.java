package app.User.Service;

import app.Exception.DomainException;
import app.Subscription.Model.Subscription;
import app.Subscription.Service.SubscriptionService;
import app.User.Model.User;
import app.User.Model.UserRole;
import app.User.Repository.UserRepository;
import app.Wallet.Service.WalletService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       SubscriptionService subscriptionService,
                       WalletService walletService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.subscriptionService = subscriptionService;
        this.walletService = walletService;
    }

    public User Login(LoginRequest loginRequest) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());
        if (!optionalUser.isPresent()) {
            throw new DomainException("User or Password is incorrect");
        }

        User user = optionalUser.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DomainException("User or Password is incorrect");
        }

        return user;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {
        Optional<User> userOptional = this.userRepository.findByUsername(registerRequest.getUsername());
        if (userOptional.isPresent()) {
            throw new DomainException("Username [%s] already exists".formatted(registerRequest.getUsername()));
        }

        User user = InitializeUser(registerRequest);

        walletService.createNewWallet(user);
        subscriptionService.createDefaultSubscription(user);
        this.userRepository.save(user);

        log.info("User created: %s".formatted(user.getUsername()));


        return user;
    }

    private User InitializeUser(RegisterRequest registerRequest) {
        LocalDateTime now = LocalDateTime.now();

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userRole(UserRole.USER)
                .isActive(true)
                .createdOn(now)
                .updatedOn(now)
                .country(registerRequest.getCountry())
                .build();
    }

    public User getById(UUID id) {
        return this.userRepository.findById(id).orElseThrow(() -> new DomainException("User not found"));
    }

    public List<Subscription> getSubscriptionHistory() {
        return this.userRepository.findById(UUID.fromString("c1d0d838-1655-4499-90ad-ea67b895845c")).get().getSubscriptions();
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
