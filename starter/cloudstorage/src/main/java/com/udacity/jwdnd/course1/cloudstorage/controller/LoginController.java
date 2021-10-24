package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public LoginController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping
    public String loginUser(@ModelAttribute User user, Model model, Authentication authentication) {

        String loginError = "";

        if (!userService.isUserNameValid(user.getUsername()) || authenticationService.authenticate(authentication) != null) {
            loginError = "Invalid username or password";
        }

        if (!loginError.equals("")) {
            model.addAttribute("loginError", loginError);
        }
        return "home";
    }
}
