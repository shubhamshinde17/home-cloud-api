package com.homecloud.api.transferobject;

import java.time.LocalDateTime;
import java.util.Optional;

public class ResponseDTO<T, U> {
    private boolean success;
    private String message;
    private LocalDateTime timestamp;
    private T data;
    private U meta;

    public ResponseDTO(boolean success, String message, Optional<T> data, Optional<U> meta) {
        this.success = success;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data.orElse(null);
        this.meta = meta.orElse(null);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public U getMeta() {
        return meta;
    }

    public void setMeta(U meta) {
        this.meta = meta;
    }
}
