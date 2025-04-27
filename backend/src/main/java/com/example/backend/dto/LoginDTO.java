package com.example.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginDTO {
    private String login;
    private String email;

    @NotNull
    @NotBlank
    private String password;
}
