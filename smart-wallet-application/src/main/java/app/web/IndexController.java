package app.web;


import app.user.Model.User;
import app.user.Service.UserService;
import app.web.dto.LoginRequest;
import app.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
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
    public ModelAndView getLoginPage() {

        return new ModelAndView("login").addObject("loginRequest", LoginRequest.builder().build());
    }

    @PostMapping("login")
    public ModelAndView postLogin(@Valid LoginRequest loginRequest, BindingResult bindingResult, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("login");
        }

        User loggedInUser = userService.loginUser(loginRequest);
        session.setAttribute("user_id", loggedInUser.getId());
        return new ModelAndView("redirect:/home").addObject("user", loggedInUser);
    }

    @GetMapping("register")
    public ModelAndView getRegisterPage() {

        return new ModelAndView("register").addObject("registerRequest", RegisterRequest.builder().build());
    }

    @PostMapping("register")
    public ModelAndView registerUser(@Valid RegisterRequest registerRequest, BindingResult result) {

        if (result.hasErrors()) {
            return new ModelAndView("register");
        }

        User registeredUser = userService.register(registerRequest);
        return new ModelAndView("redirect:/login").addObject("user", registeredUser);
    }

    @GetMapping("home")
    public ModelAndView getHomePage(@SessionAttribute("user_id") UUID userId) {

        User user = userService.getById(userId);
        return new ModelAndView("home").addObject("user", user);
    }

    @GetMapping("reports")
    public ModelAndView getReportsPage() {
        return new ModelAndView("reports");
    }

    @GetMapping("transfers")
    public ModelAndView getTransferPage() {
        return new ModelAndView("transfer");
    }

    @GetMapping("upgrade")
    public ModelAndView getUpgradePage() {
        return new ModelAndView("upgrade");
    }


}
