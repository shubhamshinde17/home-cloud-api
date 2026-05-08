package com.homecloud.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.homecloud.api.model.UploadLog;
import com.homecloud.api.model.User;

@Service
public class FileService {

    private UploadLogService uploadLogService;
    private AuthService authService;
    private UtilService utilService;
    private Config config;
    private final Logger logger;

    public FileService(UploadLogService uploadLogService, AuthService authService, UtilService utilService,
            Config config) {
        this.uploadLogService = uploadLogService;
        this.authService = authService;
        this.utilService = utilService;
        this.config = config;
        this.logger = Logger.getLogger(FileService.class.getName());
    }

    public UploadLog saveFile(MultipartFile file, String username) throws IOException {
        String folderId = utilService.generateUUIDFromEmail(username);
        logger.info("Saving file for user: " + username + " with folderId: " + folderId);
        User user = authService.getUserByEmail(username);
        Path userDir = Paths.get(config.getStoragePath(), folderId);
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
        List<UploadLog> uploadLogs = uploadLogService.getActiveUploadLogsByUsername(username);
        for (UploadLog uploadLog : uploadLogs) {
            Path filePath = Paths.get(uploadLog.getPath());
            if (!Files.exists(filePath)) {
                uploadLog.setIsDeleted(true);
                uploadLogService.updateUploadLog(uploadLog);
            }
        }
        return uploadLogService.getActiveUploadLogsByUsername(username);
    }

}
