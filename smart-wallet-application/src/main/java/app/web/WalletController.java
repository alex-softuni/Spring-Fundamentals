package app.web;

import app.user.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/wallets")
public class WalletController {
    private final UserService userService;

    public WalletController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getWallets() {

        ModelAndView modelAndView = new ModelAndView("wallets");

        return modelAndView;
    }
}
