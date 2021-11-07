package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FilesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    private final FilesMapper filesMapper;

    public FileService(FilesMapper filesMapper) {
        this.filesMapper = filesMapper;
    }

    public File getFileById(Integer fileId) {
        return filesMapper.getFile(fileId);
    }

    public List<File> getAllFilesByUserId(Integer userId) {
        return filesMapper.getAllFiles(userId);
    }

    public int uploadFile(MultipartFile multipartFile, Integer userId) throws IOException {

        File file = new File(null, multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getSize(), userId, multipartFile.getBytes());

        return filesMapper.insertFile(file);
    }

    public int deleteFileById(Integer fileId) {
        return filesMapper.deleteFile(fileId);
    }

    public int getFileCountByName(String filename) {
        return filesMapper.getFileCount(filename);
    }
}
