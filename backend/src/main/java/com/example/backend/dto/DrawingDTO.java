package com.example.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawingDTO {
    @NotNull(groups=Update.class)
    @NotBlank(groups=Update.class)
    private String grid;

    @NotNull(groups=Update.class)
    private String name;

    @NotNull
    @NotBlank
    @Min(1)
    private int size_x;

    @NotNull
    @NotBlank
    @Min(1)
    private int size_y;

    @NotNull
    @Min(1)
    private Long authorId;

    public interface Update {}
}
