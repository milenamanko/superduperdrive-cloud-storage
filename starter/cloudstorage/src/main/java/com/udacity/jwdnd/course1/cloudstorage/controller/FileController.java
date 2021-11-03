package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class FileController {

    private final FileService fileService;

    private final UserService userService;

    public FileController(UserMapper userMapper, FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }


    @ModelAttribute("fileDTO")
    public FileDTO getFileDTO() {
        return new FileDTO();
    }

//    @GetMapping("/home/files")
//    public String listUploadedFiles(Model model, Authentication authentication) throws IOException {
//
//        model.addAttribute("file", fileService.getAllFilesByUserId(userService.getUser(authentication.getName()).getUserId()));
//
//        return "home";
//    }

//    @GetMapping("/home/files/{filename:.+}")
//    @ResponseBody
//    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
//
//        Resource file = filesMapper.getFileAsResource(filename);
//
//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
//    }

    @PostMapping("/home/file/newFile")
    public String uploadFile(Authentication authentication, Model model, @ModelAttribute("fileDTO") MultipartFile file) throws IOException {

        String fileError = "";

        if (file.isEmpty()) {
            fileError = "File is empty.";
        }

        if (fileError.equals("")) {
            fileService.uploadFile(file, userService.getUser(authentication.getName()).getUserId());
        }

        if (fileError.equals("")) {
            model.addAttribute("fileSuccess", true);
        } else {
            model.addAttribute("fileError", fileError);
        }

        return "result";
    }


//    @PostMapping("/home/files/newFile")
//    public String uploadFile(@RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {
//
//        InputStream stream = file.getInputStream();
//        filesMapper.insertFile(file);
//
//        redirectAttributes.addFlashAttribute("fileSuccess", file.getName() + "has been uploaded.");
//
//        return "home";
//    }

}
