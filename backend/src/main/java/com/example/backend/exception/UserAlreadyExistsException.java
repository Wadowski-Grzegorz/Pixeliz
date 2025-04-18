package com.example.backend.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super("User with this " + message + " already exists");
    }

    public UserAlreadyExistsException(){
        super("User already exists");
    }
}
