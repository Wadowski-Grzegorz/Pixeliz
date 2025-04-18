package com.example.backend.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class RoleNotFoundException extends DetailedException {
    public RoleNotFoundException(Map<String, Object> criteria){
        super("Role not found", criteria);
    }

    public RoleNotFoundException(){
        super("Role not found");
    }
}
