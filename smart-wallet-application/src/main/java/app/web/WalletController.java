package app.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/wallets")
public class WalletController {

    @GetMapping("/{id}")
    public ModelAndView getWallets(@PathVariable UUID id) {

        ModelAndView modelAndView = new ModelAndView("wallets");

        return modelAndView;
    }
}
