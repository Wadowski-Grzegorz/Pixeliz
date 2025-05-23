package com.example.backend.dto;

import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class DrawingRoleDTO {
    private Drawing drawing;
    private Role role;
}
