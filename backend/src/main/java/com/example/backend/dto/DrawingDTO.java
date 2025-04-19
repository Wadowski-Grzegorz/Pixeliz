package com.example.backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawingDTO {
    @NotNull(groups = {Default.class, Update.class})
    @NotBlank(groups = {Default.class, Update.class})
    private String grid;

    @NotNull(groups = {Default.class, Update.class})
    private String name;

    @Min(1)
    private int size_x;

    @Min(1)
    private int size_y;

    @NotNull
    @Min(1)
    private Long authorId;

    public interface Update {}
}
