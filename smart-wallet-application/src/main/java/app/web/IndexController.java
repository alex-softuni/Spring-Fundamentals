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

        User user = userService.getById(UUID.fromString("c1d0d838-1655-4499-90ad-ea67b895845c"));
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("reports")
    public ModelAndView getReportsPage() {
        return new ModelAndView("reports");
    }


}
