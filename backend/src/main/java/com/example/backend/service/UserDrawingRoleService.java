package com.example.backend.service;

import com.example.backend.dto.DrawingRoleDTO;
import com.example.backend.dto.UserRoleDTO;
import com.example.backend.exception.RelationNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.UserDrawingRoleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserDrawingRoleService {

    private final UserDrawingRoleRepository userDrawingRoleRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final DrawingService drawingService;

    public UserDrawingRoleService(UserDrawingRoleRepository userDrawingRoleRepository,
                                  RoleService roleService,
                                  UserService userService,
                                  @Lazy DrawingService drawingService) {
        this.userDrawingRoleRepository = userDrawingRoleRepository;
        this.roleService = roleService;
        this.userService = userService;
        this.drawingService = drawingService;
    }

    public UserDrawingRole createUserDrawingRole(User user, Drawing drawing, Role role) {
        if(user == null || user.getId() == null || userService.getUser(user.getId()) == null){
            throw new RelationNotFoundException(Map.of("user", "incorrect"));
        }else if(drawing == null || drawing.getId() == null || drawingService.getDrawing(drawing.getId()) == null){
            throw new RelationNotFoundException(Map.of("drawing", "incorrect"));
        }else if(role == null || role.getId() == null || roleService.getRole(role.getId()) == null){
            throw new RelationNotFoundException(Map.of("role", "incorrect"));
        }
        return userDrawingRoleRepository.save(new UserDrawingRole(user, drawing, role));
    }

    public List<UserRoleDTO> getUsersFromDrawing(Long drawingId){
        List<Object[]> rows = userDrawingRoleRepository.findUsersAndRoles(drawingId);

        return rows.stream()
                .map(row ->{
                    User user = (User) row[0];
                    Role role = (Role) row[1];
                    return new UserRoleDTO(user.getId(), user.getName(), role);
                })
                .collect(Collectors.toList());
    }

    public UserDrawingRole getUserDrawingRole(Long drawingId, Long userId) {
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);
        return userDrawingRoleRepository.findById(key)
                .orElseThrow(() -> new RelationNotFoundException(Map.of("drawingId", drawingId, "userId", userId)));
    }

    public List<DrawingRoleDTO> getUserDrawings(Long userId) {
        List<Object[]> rows = userDrawingRoleRepository.findDrawingAndRoles(userId);

        return rows.stream()
                .map(row ->{
                    Drawing drawing = (Drawing) row[0];
                    Role role = (Role) row[1];
                    return new DrawingRoleDTO(drawing, role);
                })
                .collect(Collectors.toList());
    }

    public UserRoleDTO getUserFromDrawing(Long drawingId, Long userId){
        UserDrawingRole udr =  getUserDrawingRole(drawingId, userId);
        return toUserRoleDTO(udr);
    }

    public UserRoleDTO updateRole(Long userId, Long drawingId, Long roleId){
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


    private UserRoleDTO toUserRoleDTO(UserDrawingRole udr){
        User user = udr.getUser();
        Role role = udr.getRole();
        return new UserRoleDTO(user.getId(), user.getName(), role);
    }


}
