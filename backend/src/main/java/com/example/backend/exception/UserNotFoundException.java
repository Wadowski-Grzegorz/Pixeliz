package com.example.backend.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserNotFoundException extends DetailedException {
    public UserNotFoundException(Map<String, Object> criteria){
        super("User not found", criteria);
    }

    public UserNotFoundException(){
        super("User not found");
    }
}
