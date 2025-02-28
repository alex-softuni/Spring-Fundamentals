package app.Subscription.Service;

import app.Subscription.Model.Subscription;
import app.Subscription.Model.SubscriptionPeriod;
import app.Subscription.Model.SubscriptionStatus;
import app.Subscription.Model.SubscriptionType;
import app.Subscription.Repository.SubscriptionRepository;
import app.User.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void createDefaultSubscription(User user) {
        Subscription subscription = IntializeSubscription(user);

        this.subscriptionRepository.save(subscription);

        if (user.getSubscriptions() == null) { // Defensive check
            user.setSubscriptions(new ArrayList<>());
        }
        user.getSubscriptions().add(subscription);

    }

    private static Subscription IntializeSubscription(User user) {
        LocalDateTime now = LocalDateTime.now();

        return Subscription.builder()
                .owner(user)
                .status(SubscriptionStatus.ACTIVE)
                .period(SubscriptionPeriod.MONTHLY)
                .type(SubscriptionType.DEFAULT)
                .price(BigDecimal.ZERO)
                .renewalAllowed(true)
                .createdOn(now)
                .completedOn(now.plusMonths(1))
                .build();
    }
}
