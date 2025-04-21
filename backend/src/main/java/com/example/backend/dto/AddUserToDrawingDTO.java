package com.example.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddUserToDrawingDTO {
    @NotNull
    @NotBlank
    private Long roleId;

    @NotNull
    @NotBlank
    private String name;
}
