package com.example.backend.service;

import com.example.backend.dto.UserRoleDTO;
import com.example.backend.model.*;
import com.example.backend.repository.UserDrawingRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class UserDrawingRoleService {

    private final UserDrawingRoleRepository userDrawingRoleRepository;
    private final RoleService roleService;

    public UserDrawingRoleService(UserDrawingRoleRepository userDrawingRoleRepository, RoleService roleService) {
        this.userDrawingRoleRepository = userDrawingRoleRepository;
        this.roleService = roleService;
    }

    public UserDrawingRole createUserDrawingRole(User user, Drawing drawing, Role role) {
        return userDrawingRoleRepository.save(new UserDrawingRole(user, drawing, role));
    }

    public List<UserRoleDTO> getUsersFromDrawing(Long drawingId){
        List<Object[]> rows = userDrawingRoleRepository.findUsersAndRoles(drawingId);

        return rows.stream()
                .map(row ->{
                    User user = (User) row[0];
                    Role role = (Role) row[1];
                    return new UserRoleDTO(user.getId(), user.getUsername(), role);
                })
                .collect(Collectors.toList());
    }

    public UserDrawingRole getUserDrawingRole(Long drawingId, Long userId) {
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);
        return userDrawingRoleRepository.findById(key)
                .orElseThrow(() -> new NoSuchElementException("UserDrawingRole not found"));
    }

    public UserRoleDTO getUserFromDrawing(Long drawingId, Long userId){
        UserDrawingRole udr =  getUserDrawingRole(drawingId, userId);
        return toUserRoleDTO(udr);
    }

    public UserRoleDTO updateRole(Long drawingId, Long userId, Long roleId){
        Role newRole = roleService.getRole(roleId);
        UserDrawingRole oldUdr = getUserDrawingRole(drawingId, userId);

        oldUdr.setRole(newRole);
        UserDrawingRole newUdr = userDrawingRoleRepository.save(oldUdr);
        return toUserRoleDTO(newUdr);
    }

    public void deleteUserDrawingRole(Long drawingId, Long userId){
        UserDrawingRole udr =  getUserDrawingRole(drawingId, userId);
        userDrawingRoleRepository.delete(udr);
    }


    public UserRoleDTO toUserRoleDTO(UserDrawingRole udr){
        User user = udr.getUser();
        Role role = udr.getRole();
        return new UserRoleDTO(user.getId(), user.getUsername(), role);
    }


}
