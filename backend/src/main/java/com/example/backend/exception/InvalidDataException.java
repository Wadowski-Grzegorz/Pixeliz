package com.example.backend.exception;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(String message){
        super(message);
    }
}
