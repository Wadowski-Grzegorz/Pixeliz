package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import jakarta.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotNull()
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    @Email(groups = Update.class)
    private String email;

    @NotNull
    @NotBlank
    private String password;

    public interface Update {}
}
