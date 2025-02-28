package app.User.Model;

import app.Subscription.Model.Subscription;
import app.Wallet.Model.Wallet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String profilePicture;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column
    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column(nullable = false)
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "owner")
    private List<Wallet> wallets = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Subscription> subscriptions = new ArrayList<>();



}
