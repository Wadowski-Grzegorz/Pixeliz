package com.example.backend.service;

import com.example.backend.dto.AddUserToDrawingDTO;
import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.repository.UserDrawingRoleRepository;

public class UserDrawingRoleService {

    private final UserDrawingRoleRepository userDrawingRoleRepository;

    public UserDrawingRoleService(UserDrawingRoleRepository userDrawingRoleRepository) {
        this.userDrawingRoleRepository = userDrawingRoleRepository;
    }

    public UserDrawingRole createUserDrawingRole(User user, Drawing drawing, Role role) {
        UserDrawingRole userDrawingRole = new UserDrawingRole(user, drawing, role);
        return userDrawingRoleRepository.save(userDrawingRole);
    }
}
