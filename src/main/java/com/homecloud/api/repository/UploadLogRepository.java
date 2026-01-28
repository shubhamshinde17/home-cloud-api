package com.homecloud.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.homecloud.api.model.UploadLog;

public interface UploadLogRepository extends JpaRepository<UploadLog, Long> {
    List<UploadLog> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<UploadLog> findByIdAndUserId(Long id, Long userId);
}
