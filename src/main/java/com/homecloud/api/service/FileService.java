package com.homecloud.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.homecloud.api.model.UploadLog;
import com.homecloud.api.model.User;

@Service
public class FileService {

    @Value("${storage.path}")
    private String storageRoot;

    private UploadLogService uploadLogService;
    private AuthService authService;

    public FileService(UploadLogService uploadLogService, AuthService authService) {
        this.uploadLogService = uploadLogService;
        this.authService = authService;
    }

    public UploadLog saveFile(MultipartFile file, String username) throws IOException {
        User user = authService.getUserByEmail(username);
        Path userDir = Paths.get(storageRoot, username);
        Files.createDirectories(userDir);
        Path targetFile = userDir.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), targetFile);
        return uploadLogService.saveUploadLog(file.getOriginalFilename(), targetFile.toString(), file.getSize(), false,
                user);
    }

    public UploadLog updateFile(Long uploadLogId, String newFileName, String newPathString,
            String username) throws IOException {
        User user = authService.getUserByEmail(username);
        UploadLog uploadLog = uploadLogService.getUploadLogById(uploadLogId, user.getId()).orElse(null);
        if (uploadLog == null) {
            throw new IOException("File not found");
        }
        Path oldFilePath = Paths.get(uploadLog.getPath());
        Path newFilePath = Paths.get(newPathString);
        if (newFileName != null && !newFileName.isEmpty()) {
            newFilePath = oldFilePath.resolveSibling(newFileName);
            Files.move(oldFilePath, newFilePath);
            uploadLog.setName(newFileName);
            uploadLog.setPath(newFilePath.toString());
            uploadLog.setIsDeleted(false);
            uploadLog.setUser(user);
            uploadLog.setSize(Files.size(newFilePath));
        }
        return uploadLogService.updateUploadLog(uploadLog);
    }

    public UploadLog deleteFile(Long uploadLogId, String username) throws IOException {
        User user = authService.getUserByEmail(username);
        UploadLog uploadLog = uploadLogService.getUploadLogById(uploadLogId, user.getId()).orElse(null);
        if (uploadLog == null) {
            throw new IOException("File not found");
        }
        Path filePath = Paths.get(uploadLog.getPath());
        Files.deleteIfExists(filePath);
        uploadLog.setIsDeleted(true);
        uploadLog.setUser(user);
        return uploadLogService.updateUploadLog(uploadLog);
    }

    public List<UploadLog> listFiles(String username) throws IOException {
        return uploadLogService.getActiveUploadLogsByUsername(username);
    }

}
