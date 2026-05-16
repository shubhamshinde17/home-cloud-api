package com.homecloud.api.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.homecloud.api.enums.COMMON_MESSAGES;
import com.homecloud.api.transferobject.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO<Void, String>> handleAllExceptions(Exception ex) {
        ResponseDTO<Void, String> response = new ResponseDTO<Void, String>(false,
                COMMON_MESSAGES.INTERNAL_SERVER_ERROR.toString(), null, ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}
