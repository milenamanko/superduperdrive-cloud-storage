package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.dto.FileDTO;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

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

    @PostMapping("/home/file/newFile")
    public String uploadFile(Authentication authentication, Model model, @ModelAttribute("fileDTO") MultipartFile file) throws IOException {

        String fileError = "";

        if (file.isEmpty()) {
            fileError = "File is empty.";
        }

        if (fileService.getFileCountByName(file.getOriginalFilename()) >= 1) {
            fileError = "File with this name already exists.";
        }

        if (fileError.equals("")) {

            int fileCount = fileService.uploadFile(file, userService.getUser(authentication.getName()).getUserId());
            if (fileCount < 1) {
                fileError = "An error has occurred while uploading. Please try again.";
            }
        }

        if (fileError.equals("")) {
            model.addAttribute("fileSuccess", "File has been uploaded successfully.");
        } else {
            model.addAttribute("fileError", fileError);
        }

        return "result";
    }


    @GetMapping("/home/file/{fileId}")
    @ResponseBody
    public ResponseEntity<InputStreamResource> serveFile(@PathVariable Integer fileId) throws IOException, SQLException {

//        Resource file = fileService.getFileAsResource(filename);
        File file = fileService.getFileById(fileId);

        Blob blob = new SerialBlob(file.getFileData());

//        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(new InputStreamResource(file.getInputStream()));

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .contentLength(file.getFileSize())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new InputStreamResource(blob.getBinaryStream()));
    }


    @RequestMapping(value = "/home/file/delete/{fileId}", method = RequestMethod.GET)
    public String deleteFile(@PathVariable Integer fileId, Model model) {

        String deleteError = "";

        if (fileService.getFileById(fileId) == null) {
            deleteError = "File doesn't exist.";
        }

        if (deleteError.equals("")) {

            int fileCount = fileService.deleteFileById(fileId);
            if (fileCount < 1) {
                deleteError = "An error has occurred while deleting. Please try again.";
            }
        }

        if (deleteError.equals("")) {
            model.addAttribute("fileSuccess", "File has been deleted successfully.");
        } else {
            model.addAttribute("fileError", deleteError);
        }

        return "result";
    }
}
