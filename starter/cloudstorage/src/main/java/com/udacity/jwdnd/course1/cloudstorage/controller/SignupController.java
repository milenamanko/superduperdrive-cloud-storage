package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    public SignupController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getSignupPage() {
        return "signup";
    }

    @PostMapping
    public String signupUser(@ModelAttribute User user, Model model) {
        String signupError = "";

        if (!userService.isUserNameAvailable(user.getUsername())) {
            signupError = "This username already exists.";
        }

        if (signupError.equals("")) {

            int rowAdded = userService.createUser(user);

            if (rowAdded < 0) {
                signupError = "An unexpected error occurred during signup process, please try again.";
            }
        }

        if (signupError.equals("")) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupError);
        }
        return "signup";
    }
}
