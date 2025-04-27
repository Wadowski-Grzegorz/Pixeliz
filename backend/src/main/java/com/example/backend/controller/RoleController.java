package com.example.backend.controller;

import com.example.backend.exception.RoleNotFoundException;
import com.example.backend.model.Role;
import com.example.backend.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(summary = "Get all roles")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Role.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.OK);
    }

    @Operation(summary = "Get specific role")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Role.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = RoleNotFoundException.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRole(
            @Parameter(description = "ID of role to be fetched")
            @PathVariable Long id){
        Role role = roleService.getRole(id);
        return new ResponseEntity<>(role, HttpStatus.OK);
    }
}
