package com.example.backend.exception;

import java.util.Map;

public class PermissionDeniedException extends DetailedException{
    public PermissionDeniedException(Map<String, Object> criteria){
        super("Permission denied", criteria);
    }

    public PermissionDeniedException(){
        super("Permission denied");
    }

}
