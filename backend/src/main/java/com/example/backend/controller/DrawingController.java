package com.example.backend.controller;

import com.example.backend.dto.*;
import com.example.backend.exception.ApiExceptionResponse;
import com.example.backend.model.Drawing;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.service.DrawingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drawing")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Drawing")
@SecurityRequirement(name = "bearerAuth")
public class DrawingController {

    private final DrawingService drawingService;

    public DrawingController(DrawingService drawingService) {
        this.drawingService = drawingService;
    }

    @Operation(
            summary = "Create a new drawing",
            description = "Creates a new drawing assigned to user which sent request."
                + " This user have all rights to this drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully created drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @PostMapping()
    public ResponseEntity<Drawing> createDrawing(
            @RequestBody @Valid DrawingDTO drawingDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Drawing drawing = drawingService.createDrawing(drawingDTO, username);
        return new ResponseEntity<>(drawing, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get specific drawing",
            description = "Returns a drawing with a role for user that sent request."
                + " Works when user which sent request have permissions for read a drawing."
                + " Admin can see all drawings"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DrawingRoleDTO> getDrawing(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        DrawingRoleDTO drawingRoleDTO = drawingService.getDrawing(id, username, isAdmin);
        return new ResponseEntity<>(drawingRoleDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all drawings",
            description = "Get all existing drawings from all users. Only for admin"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful, returns all drawings",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Drawing.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized, only admin can request",
                    content = @Content()
            )
    })
    @GetMapping()
    public ResponseEntity<List<Drawing>> getDrawings() {
        return new ResponseEntity<>(drawingService.getDrawings(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update specific drawing",
            description = "Can only update name and pixels fields of specific drawing."
                + " User that called need to have permissions for editing a drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns updated drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized, only admin can request",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Drawing> putDrawing(
            @PathVariable Long id,
            @RequestBody @Validated(DrawingDTO.Update.class) DrawingDTO drawingUpdate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Drawing drawing = drawingService.updateDrawing(id, drawingUpdate, username);
        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete specific drawing",
            description = "Delete a drawing, user which sent request must have permissions for delete."
                    + " Admin can delete all drawings"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted drawing",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrawing(@PathVariable Long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        drawingService.deleteDrawing(id, username, isAdmin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Get drawings assigned to user",
            description = "Get all drawings from user that sent a request"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful, returns drawings with a role for each drawing",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Drawing.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @GetMapping("/me")
    public ResponseEntity<List<DrawingRoleDTO>> getUserDrawings() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new ResponseEntity<>(drawingService.getUserDrawings(username), HttpStatus.OK);
    }



    @Operation(
            summary = "Give user specific permission to access a drawing",
            description = "Create new relation between user-drawing. " +
                    "Assign role which specify what user can do with this drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully assigned access for drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such drawing, user or user called ",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Unauthorized, need JWT token or user that sent request"
                        + " don't have permission to admin a drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            )
    })
    @PostMapping("/{id}/user")
    public ResponseEntity<?> addUserToDrawing(
            @PathVariable Long id,
            @RequestBody @Valid AddUserToDrawingDTO addUserToDrawingDTO
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDrawingRole relation = drawingService.addUserToDrawing(id, addUserToDrawingDTO, username);
        return new ResponseEntity<>(relation, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Get all users assigned to specific drawing",
            description = "Can be used by any user to get list of users with their permissions to a drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got list of users and roles",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRoleDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such drawing",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token with permission to moderate",
                    content = @Content()
            )
    })
    @GetMapping("/{id}/user")
    public ResponseEntity<List<UserRoleDTO>> getUsersFromDrawing(@PathVariable Long id){
        return new ResponseEntity<>(drawingService.getDrawingUsers(id), HttpStatus.OK);
    }

    @Operation(
            summary = "Get specific user assigned to specific drawing",
            description = "Can be used by any user to get user with their permission to a drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got user and his role",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User or drawing not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<UserRoleDTO> getUserFromDrawing(
            @Parameter(description = "ID of drawing")
            @PathVariable Long id,
            @Parameter(description = "ID of user")
            @PathVariable Long userId){
        return new ResponseEntity<>(drawingService.getDrawingUser(id, userId), HttpStatus.OK);
    }

    @Operation(
            summary = "Update role of a user with a drawing",
            description = "Update existing relation between user and drawing."
                + " User which make request must have permissions to moderate a drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully edited relation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such relation",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token with permission to moderate",
                    content = @Content()
            )
    })
    @PutMapping("/{id}/user/{userId}")
    public ResponseEntity<?> updateUserDrawingRole(
            @Parameter(description = "ID of drawing")
            @PathVariable Long id,
            @Parameter(description = "ID of user")
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserDrawingRoleDTO updateUserDrawingRoleDTO
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return new ResponseEntity<>(
                drawingService.updateUserDrawingRole(userId, id, updateUserDrawingRoleDTO, username),
                HttpStatus.OK);
    }

    @Operation(
            summary = "Delete specific relation between user and drawing",
            description = "Delete existing relation between user and drawing."
                    + " User which make request must have permissions to moderate a drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted a relation",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No such relation found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token with permission to moderate",
                    content = @Content()
            )
    })
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteUserFromDrawing(
            @Parameter(description = "ID of drawing")
            @PathVariable Long id,
            @Parameter(description = "ID of user")
            @PathVariable Long userId
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        drawingService.deleteUserDrawingRole(id, userId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
