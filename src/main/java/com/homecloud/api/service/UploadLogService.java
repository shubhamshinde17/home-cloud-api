package com.homecloud.api.service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

import com.homecloud.api.model.UploadLog;
import com.homecloud.api.model.User;
import com.homecloud.api.repository.UploadLogRepository;

@Service
public class UploadLogService {

    private final static Logger logger = Logger.getLogger(UploadLogService.class.getName());
    private final UploadLogRepository uploadLogRepository;
    private final AuthService authService;

    public UploadLogService(UploadLogRepository uploadLogRepository, AuthService authService) {
        this.uploadLogRepository = uploadLogRepository;
        this.authService = authService;
    }

    public UploadLog saveUploadLog(String name, String path, Long size, Boolean isFolder, User user) {
        UploadLog uploadLog = new UploadLog(name, path, size, isFolder, user);
        return uploadLogRepository.save(uploadLog);
    }

    public UploadLog updateUploadLog(UploadLog uploadLog) {
        try {
            logger.info("Updating UploadLog with id: " + uploadLog.getId());
            var id = uploadLog.getId() != null ? uploadLog.getId() : 0L;
            UploadLog existingLog = uploadLogRepository.findById(id)
                    .orElseThrow(
                            () -> new IllegalArgumentException("UploadLog not found with id: " + uploadLog.getId()));
            existingLog.setName(uploadLog.getName());
            existingLog.setPath(uploadLog.getPath());
            existingLog.setSize(uploadLog.getSize());
            existingLog.setIsFolder(uploadLog.getIsFolder());
            return uploadLogRepository.save(existingLog);
        } catch (Exception e) {
            logger.severe("Error updating UploadLog: " + e.getMessage());
        }
        return uploadLog;
    }

    public List<UploadLog> getActiveUploadLogsByUsername(String username) {
        User user = authService.getUserByEmail(username);
        Long userId = user.getId();
        return uploadLogRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public Optional<UploadLog> getUploadLogById(Long id, Long userId) {
        if (id == null) {
            throw new IllegalArgumentException("UploadLog id cannot be null");
        }
        return uploadLogRepository.findByIdAndUserId(id, userId);
    }
}
