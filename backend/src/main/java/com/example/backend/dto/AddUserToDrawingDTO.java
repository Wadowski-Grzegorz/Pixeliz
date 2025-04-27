package com.example.backend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUserToDrawingDTO {
    @NotNull
    private Long roleId;

    @NotNull
    @NotBlank
    private String name;
}
