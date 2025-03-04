package app.web;


import app.User.Model.User;
import app.User.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/")
public class IndexController {

    private final UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("register")
    public String getRegisterPage() {
        return "register";
    }

    @GetMapping("home")
    public ModelAndView getHomePage() {
        ModelAndView modelAndView = new ModelAndView("home");
        User user = userService.getById(UUID.fromString("40642ef3-3a2c-4980-a587-d631530a69c1"));
        modelAndView.addObject("user", user);

        return modelAndView;
    }


}
