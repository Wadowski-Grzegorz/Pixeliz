package com.example.backend.service;

import com.example.backend.dto.*;
import com.example.backend.exception.DrawingNotFoundException;
import com.example.backend.exception.PermissionDeniedException;
import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.repository.DrawingRepository;
import com.example.backend.repository.UserDrawingRoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final UserDrawingRoleService userDrawingRoleService;
    private final UserDrawingRoleRepository userDrawingRoleRepository;

    public DrawingService(
            DrawingRepository drawingRepository,
            UserService userService,
            RoleService roleService, UserDrawingRoleService userDrawingRoleService, UserDrawingRoleRepository userDrawingRoleRepository) {
        this.drawingRepository = drawingRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.userDrawingRoleService = userDrawingRoleService;
        this.userDrawingRoleRepository = userDrawingRoleRepository;
    }

    private interface Checker{
        boolean canWhat(Role role);
    }

    // check if user can do something with drawing
    private void checkDrawing(Long id, String username, Checker checker, String type){
        User user = userService.getUserByUsername(username);
        UserDrawingRole udr = userDrawingRoleService.getUserDrawingRole(id, user.getId());

        if(!checker.canWhat(udr.getRole())){
            throw new PermissionDeniedException(Map.of(type, false));
        }
    }

    public Drawing createDrawing(DrawingDTO drawingDTO, String username) {
        Drawing drawing = new Drawing();
        drawing.setName(drawingDTO.getName());
        drawing.setPixels(drawingDTO.getPixels());
        drawing.setSize_x(drawingDTO.getSize_x());
        drawing.setSize_y(drawingDTO.getSize_y());

        // add author to drawing
        User user = userService.getUserByUsername(username);
        Role role = roleService.getRole("owner");
        Drawing createdDrawing = drawingRepository.save(drawing);
        UserDrawingRole userDrawingRole =
                userDrawingRoleService.createUserDrawingRole(user, createdDrawing, role);

        return createdDrawing;
    }

    public Drawing getDrawing(Long id) {
        return drawingRepository
                .findById(id)
                .orElseThrow(() -> new DrawingNotFoundException(Map.of("id", id)));
    }

    public DrawingRoleDTO getDrawing(Long id, String username, boolean isAdmin){
        if(!isAdmin){
            checkDrawing(id, username, roleService::canRead, "read");
            User user = userService.getUserByUsername(username);
            UserDrawingRole udr = userDrawingRoleService.getUserDrawingRole(id, user.getId());
            return new DrawingRoleDTO(udr.getDrawing(), udr.getRole());
        }else{
            return new DrawingRoleDTO(getDrawing(id), Role.builder().name("owner").build());
        }
    }

    public List<Drawing> getDrawings(){
        return drawingRepository.findAll();
    }

    public List<DrawingRoleDTO> getUserDrawings(String username){
        Long userId = userService.getUserByUsername(username).getId();
        return userDrawingRoleService.getUserDrawings(userId);
    }

    public Drawing updateDrawing(Long id, DrawingDTO drawingUpdate, String username) {
        Drawing drawing = getDrawing(id);

        checkDrawing(id, username, roleService::canWrite, "write");

        if(drawingUpdate.getName() != null){
            drawing.setName(drawingUpdate.getName());
        }
        if(drawingUpdate.getPixels() != null && !drawingUpdate.getPixels().isEmpty()){
            drawing.setPixels(drawingUpdate.getPixels());
        }

        return drawingRepository.save(drawing);
    }

    public void deleteDrawing(Long id, String username, boolean isAdmin){
        getDrawing(id);
        if(!isAdmin){
            checkDrawing(id, username, roleService::canDelete, "delete");
        }
        drawingRepository.deleteById(id);
    }


    public UserDrawingRole addUserToDrawing(Long id, AddUserToDrawingDTO addUserToDrawingDTO, String username) {
        checkDrawing(id, username, roleService::canAdmin, "admin");

        User user = userService.getUserByName(addUserToDrawingDTO.getName());
        Role role = roleService.getRole(addUserToDrawingDTO.getRoleId());
        Drawing drawing = getDrawing(id);

        return userDrawingRoleService.createUserDrawingRole(user, drawing, role);
    }

    public List<UserRoleDTO> getDrawingUsers(Long drawingId){
        return userDrawingRoleService.getUsersFromDrawing(drawingId);
    }

    public UserRoleDTO getDrawingUser(Long drawingId, Long userId){
        return userDrawingRoleService.getUserFromDrawing(drawingId, userId);
    }

    public UserRoleDTO updateUserDrawingRole(Long userId, Long drawingId, UpdateUserDrawingRoleDTO updateUserDrawingRoleDTO, String username){
        checkDrawing(drawingId, username, roleService::canAdmin, "admin");
        return userDrawingRoleService.updateRole(userId, drawingId, updateUserDrawingRoleDTO.getRoleId());
    }

    public void deleteUserDrawingRole(Long drawingId, Long userId, String username){
        checkDrawing(drawingId, username, roleService::canAdmin, "admin");
        userDrawingRoleService.deleteUserDrawingRole(drawingId, userId);
    }
}
