package app.web;

import app.user.Model.User;
import app.user.Service.UserService;
import app.web.dto.EditProfileRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUsers() {

        return new ModelAndView("users").addObject("users", userService.getAllUsers());
    }

    @GetMapping("/profile")
    public ModelAndView getProfileMenu(@SessionAttribute("user_id") UUID userId) {

        User user = userService.getById(userId);
        return new ModelAndView("profile-menu").addObject("editProfileRequest", EditProfileRequest.builder().build());
    }

    @PutMapping("/profile")
    public ModelAndView editProfileMenu(@SessionAttribute("user_id") UUID userId, @Valid EditProfileRequest editProfileRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            User user = userService.getById(userId);

            return new ModelAndView("profile-menu").addObject("editProfileRequest", editProfileRequest).addObject("user", user);
        }

        userService.editUserProfile(userId,editProfileRequest);

        return new ModelAndView("redirect:/home");
    }


}
