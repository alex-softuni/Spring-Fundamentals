package app.web;

import app.subscription.Model.Subscription;
import app.user.Model.User;
import app.user.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final UserService userService;

    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUpgrade(@SessionAttribute("user_id") UUID userId) {

        User user = userService.getById(userId);
        return new ModelAndView("upgrade").addObject("user", user);
    }

    @GetMapping("/history")
    public ModelAndView getHistory(@SessionAttribute("user_id") UUID userId) {

        List<Subscription> history = userService.getById(userId).getSubscriptions();
        return new ModelAndView("subscription-history").addObject("history", history);
    }
}

