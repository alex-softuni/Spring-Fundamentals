package app.user.Service;

import app.exception.DomainException;
import app.subscription.Service.SubscriptionService;
import app.user.Model.User;
import app.user.Model.UserRole;
import app.user.Repository.UserRepository;
import app.wallet.Service.WalletService;
import app.web.dto.EditProfileRequest;
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

    public void editUserProfile(UUID userId, EditProfileRequest editProfileRequest) {

        User user = getById(userId);
        user.setFirstName(editProfileRequest.getFirstName());
        user.setLastName(editProfileRequest.getLastName());
        user.setEmail(editProfileRequest.getEmail());
        user.setProfilePicture(editProfileRequest.getProfilePicture());
        user.setUpdatedOn(LocalDateTime.now());
        this.userRepository.save(user);
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


    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }


    public User loginUser(LoginRequest loginRequest) {
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
}
