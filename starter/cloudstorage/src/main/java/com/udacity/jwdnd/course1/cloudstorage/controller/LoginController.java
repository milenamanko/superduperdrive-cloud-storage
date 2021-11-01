package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final SecurityService securityService;

    @Autowired
    public LoginController( SecurityService securityService) {
        this.securityService = securityService;
    }

    @GetMapping("/login")
    public String login(Model model, String error) {

        if (securityService.isAuthenticated()) {
            return "home";
        }

        if (error != null)
            model.addAttribute("loginError", "Your username or password is invalid.");

        return "login";
    }

//    @PostMapping("/login")
//    public String loginUser(@ModelAttribute User user, Model model, Authentication authentication) {
//        String loginError = "";
//
//        if (!userService.isUserNameValid(user.getUsername()) || authenticationService.authenticate(authentication) != null) {
//            loginError = "Invalid username or password";
//        }
//
//        if (!loginError.equals("")) {
//            model.addAttribute("loginError", loginError);
//        }
//        return "home";
//    }
}
