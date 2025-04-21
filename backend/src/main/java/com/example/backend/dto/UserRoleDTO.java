package com.example.backend.dto;

import com.example.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRoleDTO {
    private Long userId;
    private String name;

    private Role role;
}
