package com.homecloud.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.homecloud.api.model.UploadLog;
import com.homecloud.api.service.FileService;
import com.homecloud.api.transferobject.ObjectResponseDTO;

import jakarta.websocket.server.PathParam;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/files")
public class FileController {

    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<ObjectResponseDTO<UploadLog>> uploadFile(@RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        UploadLog uploadLog = fileService.saveFile(file, authentication.getName());

        return ResponseEntity.ok(
                new ObjectResponseDTO<>(true, "File uploaded successfully", Optional.of(uploadLog), Optional.empty()));
    }

    @GetMapping("/list")
    public ResponseEntity<ObjectResponseDTO<List<UploadLog>>> getFiles(Authentication authentication)
            throws IOException {
        List<UploadLog> files = fileService.listFiles(authentication.getName());
        return ResponseEntity.ok(
                new ObjectResponseDTO<>(true, "Files retrieved successfully", Optional.of(files), Optional.empty()));
    }

    @PostMapping("/{uploadLogId}/update")
    public ResponseEntity<ObjectResponseDTO<UploadLog>> updateFile(@PathParam("uploadLogId") Long uploadLogId,
            @RequestBody HashMap<String, String> updates,
            Authentication authentication) throws IOException {

        String newFileName = updates.get("fileName");
        String newPath = updates.get("path");
        UploadLog uploadLog = fileService.updateFile(uploadLogId, newFileName, newPath, authentication.getName());
        return ResponseEntity.ok(
                new ObjectResponseDTO<>(true, "File updated successfully", Optional.of(uploadLog), Optional.empty()));
    }

    @DeleteMapping("/{uploadLogId}/delete")
    public ResponseEntity<ObjectResponseDTO<String>> deleteFile(@PathParam("uploadLogId") Long uploadLogId,
            Authentication authentication)
            throws IOException {
        fileService.deleteFile(uploadLogId, authentication.getName());
        return ResponseEntity.ok(
                new ObjectResponseDTO<>(true, "File deleted successfully", Optional.empty(), Optional.empty()));
    }

}
