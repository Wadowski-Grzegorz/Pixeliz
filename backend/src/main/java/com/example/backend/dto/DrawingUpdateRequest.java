package com.example.backend.dto;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class DrawingUpdateRequest {
    private String grid;
    private String name;
}
