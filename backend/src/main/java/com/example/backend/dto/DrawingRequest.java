package com.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawingRequest {
    private String grid;
    private String name;
    private int size_x;
    private int size_y;
}
