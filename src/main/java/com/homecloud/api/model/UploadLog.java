package com.homecloud.api.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "upload_logs")
public class UploadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    private Long size;
    private Boolean isFolder;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    protected UploadLog() {
    }

    public UploadLog(String name, String path, Long size, Boolean isFolder, User user) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.isFolder = isFolder;
        this.isDeleted = false;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Long getSize() {
        return size;
    }

    public Boolean getIsFolder() {
        return isFolder;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setIsFolder(Boolean isFolder) {
        this.isFolder = isFolder;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UploadLog [id=" + id + ", name=" + name + ", path=" + path + ", size=" + size + ", isFolder=" + isFolder
                + ", createdAt=" + createdAt + ", modifiedAt=" + modifiedAt + ", isDeleted=" + isDeleted + "]";
    }
}
