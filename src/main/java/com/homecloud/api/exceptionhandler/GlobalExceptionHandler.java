package com.homecloud.api.exceptionhandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.homecloud.api.transferobject.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleAllExceptions(Exception ex) {
        ResponseDTO response = new ResponseDTO(false, "An error occurred: " + ex.getMessage());
        return ResponseEntity.status(500).body(response);
    }
}
