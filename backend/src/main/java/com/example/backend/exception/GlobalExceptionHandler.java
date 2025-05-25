package com.example.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;


import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private HttpStatus getStatus(DetailedException ex) {
        if (ex instanceof PermissionDeniedException) {
            return HttpStatus.FORBIDDEN;
        }
        if (ex instanceof DrawingNotFoundException
                || ex instanceof UserNotFoundException
                || ex instanceof RoleNotFoundException
                || ex instanceof RelationNotFoundException
                || ex instanceof UserStatisticsNotFoundException) {
            return HttpStatus.NOT_FOUND;
        }

        return HttpStatus.BAD_REQUEST;
    }

    @ExceptionHandler(DetailedException.class)
    public ResponseEntity<Object> handleDetailedException(DetailedException ex){
        HttpStatus status = getStatus(ex);
        ApiExceptionResponse apiEx = new ApiExceptionResponse(ex.getMessage(), status.value(), ex.getCriteria());

        return new ResponseEntity<>(apiEx, status);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiExceptionResponse apiEx = new ApiExceptionResponse(ex.getMessage(), status.value());

        return new ResponseEntity<>(apiEx, status);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        HttpStatus status = HttpStatus.CONFLICT;
        ApiExceptionResponse apiEx = new ApiExceptionResponse(ex.getMessage(), status.value());

        return new ResponseEntity<>(apiEx, status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, Object> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(
                                FieldError::getDefaultMessage,
                                Collectors.collectingAndThen(Collectors.toList(), list -> (Object) list)
                        )
                ));

        ApiExceptionResponse apiEx = new ApiExceptionResponse(
                "Invalid arguments",
                status.value(),
                errors
                );

        return new ResponseEntity<>(apiEx, status);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiExceptionResponse apiEx = new ApiExceptionResponse(ex.getMessage(), status.value());

        return new ResponseEntity<>(apiEx, status);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiExceptionResponse apiEx = new ApiExceptionResponse("Invalid credentials", status.value());
        return new ResponseEntity<>(apiEx, status);
    }
}
