package com.example.backend.service;

import com.example.backend.dto.DrawingDTO;
import com.example.backend.dto.UserRoleDTO;
import com.example.backend.exception.DrawingNotFoundException;
import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.repository.DrawingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final UserDrawingRoleService userDrawingRoleService;

    public DrawingService(
            DrawingRepository drawingRepository,
            UserService userService,
            RoleService roleService, UserDrawingRoleService userDrawingRoleService) {
        this.drawingRepository = drawingRepository;
        this.userService = userService;
        this.roleService = roleService;
        this.userDrawingRoleService = userDrawingRoleService;
    }

    public Drawing createDrawing(DrawingDTO drawingDTO) {
        Drawing drawing = new Drawing();
        drawing.setName(drawingDTO.getName());
        drawing.setGrid(drawingDTO.getGrid());
        drawing.setSize_x(drawingDTO.getSize_x());
        drawing.setSize_y(drawingDTO.getSize_y());

        // if drawing have author
        if(drawingDTO.getAuthorId() > 0){
            User user = userService.getUser(drawingDTO.getAuthorId());
            Role role = roleService.getRole("owner");
            UserDrawingRole userDrawingRole =
                    userDrawingRoleService.createUserDrawingRole(user, drawing, role);
        }
        return drawingRepository.save(drawing);
    }

    public Drawing getDrawing(Long id) {
        return drawingRepository
                .findById(id)
                .orElseThrow(() -> new DrawingNotFoundException(Map.of("id", id)));
    }

    public List<Drawing> getDrawings(){
        return drawingRepository.findAll();
    }

    public Drawing updateDrawing(Long id, DrawingDTO drawingUpdate) {
        Drawing drawing = getDrawing(id);
        if(drawingUpdate.getName() != null){
            drawing.setName(drawingUpdate.getName());
        }
        if(drawingUpdate.getGrid() != null && !drawingUpdate.getGrid().isBlank()){
            drawing.setGrid(drawingUpdate.getGrid());
        }
        return drawingRepository.save(drawing);
    }

    public void deleteDrawing(Long id){
        getDrawing(id);
        drawingRepository.deleteById(id);
    }

    public UserDrawingRole addUserToDrawing(Long id, Long roleId, String username) {
        User user = userService.getUser(username);
        Drawing drawing = getDrawing(id);
        Role role = roleService.getRole(roleId);

        return userDrawingRoleService.createUserDrawingRole(user, drawing, role);
    }

    public List<UserRoleDTO> getDrawingUsers(Long drawingId){
        return userDrawingRoleService.getUsersFromDrawing(drawingId);
    }

    public UserRoleDTO getDrawingUser(Long drawingId, Long userId){
        return userDrawingRoleService.getUserFromDrawing(drawingId, userId);
    }

    public UserRoleDTO updateUserDrawingRole(Long drawingId, Long userId, Long roleId){
        return userDrawingRoleService.updateRole(roleId, userId, drawingId);
    }

    public void deleteUserDrawingRole(Long drawingId, Long userId){
        userDrawingRoleService.deleteUserDrawingRole(drawingId, userId);
    }

}
