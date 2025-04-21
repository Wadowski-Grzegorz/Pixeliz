package com.example.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.*;
import jakarta.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    @Email(groups = {Default.class, DrawingDTO.Update.class})
    private String email;

    @NotNull
    @NotBlank
    private String password;

    public interface Update {}
}
