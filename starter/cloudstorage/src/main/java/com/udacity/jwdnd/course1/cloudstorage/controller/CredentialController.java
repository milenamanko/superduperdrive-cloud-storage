package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class CredentialController {

    public final CredentialService credentialService;

    public final UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService, HomeController homeController) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping("/home/credential/newCredential")
    public String addCredential(Authentication authentication, Model model, Credential credential) {

        String credentialError = "";

        if (credential.getCredentialId() == null) {
            //add credential if new

            if (credential.getUrl().equals("")) {
                credentialError += "Please specify credential URL.";
            }
            if (credential.getUsername().equals("")) {
                credentialError += "Please specify credential username.";
            }
            if (credential.getPassword().equals("")) {
                credentialError += "Please specify credential password.";
            }

            if (credentialError.equals("")) {

                credential.setUserId(userService.getUser(authentication.getName()).getUserId());

                int credentialCount = credentialService.insertCredential(credential, authentication);
                if (credentialCount < 1) {
                    credentialError = "An error has occurred while adding credential. Please try again.";
                }
            }

            if (credentialError.equals("")) {
                model.addAttribute("successMsg", "Credential has been added successfully.");
            } else {
                model.addAttribute("errorMsg", credentialError);
            }
        } else {
            //update credential if ID exists

            int credentialCount = credentialService.updateCredential(credential, authentication);

            if (credentialCount < 1) {
                credentialError = "An error has occurred while editing credential. Please try again.";
            }

            if (credentialError.equals("")) {
                model.addAttribute("successMsg", "Credential has been updated successfully.");
            } else {
                model.addAttribute("errorMsg", credentialError);
            }
        }
        return "result";
    }

    @RequestMapping(value = "/home/credential/delete/{credentialId}", method = RequestMethod.GET)
    public String deleteCredential(@PathVariable Integer credentialId, Model model) {

        String credentialError = "";

        if (credentialService.getCredentialById(credentialId) == null) {
            credentialError = "Credential doesn't exist.";
        }

        if (credentialError.equals("")) {

            int credentialCount = credentialService.deleteCredentialById(credentialId);

            if (credentialCount < 1) {
                credentialError = "An error has occurred while deleting. Please try again.";
            }
        }

        if (credentialError.equals("")) {
            model.addAttribute("successMsg", "Credential has been deleted successfully.");
        } else {
            model.addAttribute("errorMsg", credentialError);
        }
        return "result";
    }
}
