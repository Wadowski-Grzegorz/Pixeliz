package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiExceptionResponse apiEx = new ApiExceptionResponse(ex.getMessage(), status.value());

        return new ResponseEntity<>(apiEx, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiExceptionResponse apiEx = new ApiExceptionResponse(ex.getMessage(), status.value(), ex.getCriteria());

        return new ResponseEntity<>(apiEx, HttpStatus.NOT_FOUND);
    }
}
