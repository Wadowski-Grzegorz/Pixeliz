package com.example.backend.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserNotFoundException extends RuntimeException{
    private final Map<String, Object> criteria;
    public UserNotFoundException(Map<String, Object> criteria){
        super("User not found");
        this.criteria = criteria;
    }
}
