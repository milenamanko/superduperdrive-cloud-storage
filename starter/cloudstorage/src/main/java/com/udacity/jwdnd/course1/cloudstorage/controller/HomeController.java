package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping
public class HomeController {

    private final UserService userService;

    private final FileService fileService;

    private final NoteService noteService;

    private final CredentialService credentialService;

    private final EncryptionService encryptionService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService, CredentialService credentialService, EncryptionService encryptionService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

    @GetMapping("/home")
    public String homePage(Model model, Authentication authentication, @ModelAttribute("fileDTO") MultipartFile file) {

        Integer loggedUserId = userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("files", fileService.getAllFilesByUserId(loggedUserId));
        model.addAttribute("notes", noteService.getAllNotesByUserId(loggedUserId));
        model.addAttribute("credentials", credentialService.getAllCredentialsByUserId(loggedUserId));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            model.addAttribute("logoutSuccess", true);
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        return "login";
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Authentication authentication) {
        return authentication.getName();
    }
}
