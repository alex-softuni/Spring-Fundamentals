package app.web;

import app.Subscription.Model.Subscription;
import app.User.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final UserService userService;

    public SubscriptionController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUpgrade() {
        ModelAndView modelAndView = new ModelAndView("upgrade");

        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView getHistory() {
        ModelAndView modelAndView = new ModelAndView("subscription-history");
        List<Subscription> history = userService.getSubscriptionHistory();
        modelAndView.addObject("history", history);
        return modelAndView;
    }
}

