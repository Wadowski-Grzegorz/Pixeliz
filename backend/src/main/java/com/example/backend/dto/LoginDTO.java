package com.example.backend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String login;
    private String email;
    private String password;
}
