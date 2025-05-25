package com.example.backend.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{
    public UserAlreadyExistsException(String message){
        super(message);
    }

    public UserAlreadyExistsException(){
        super("User already exists");
    }
}
