package com.example.backend.dto;

import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DrawingRoleDTO {
    private Drawing drawing;
    private Role role;
}
