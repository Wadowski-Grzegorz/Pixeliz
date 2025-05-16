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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
            summary = "Create drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @PostMapping()
    public ResponseEntity<Drawing> createDrawing(
            @RequestBody @Valid DrawingDTO drawingDTO,
            Principal principal) {
        String username = principal.getName();
        Drawing drawing = drawingService.createDrawing(drawingDTO, username);
        return new ResponseEntity<>(drawing, HttpStatus.CREATED);
    }

    @Operation(summary = "Get specific drawing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<DrawingRoleDTO> getDrawing(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));

        DrawingRoleDTO drawingRoleDTO = drawingService.getDrawing(id, username, isAdmin);
        return new ResponseEntity<>(drawingRoleDTO, HttpStatus.OK);
    }

    @Operation(summary = "Get all drawings")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Drawing.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @GetMapping()
    public ResponseEntity<List<Drawing>> getDrawings() {
        return new ResponseEntity<>(drawingService.getDrawings(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update specific drawing",
            description = "Can only update name and pixels fields of specific drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Drawing> putDrawing(
            @PathVariable Long id,
            @RequestBody @Validated(DrawingDTO.Update.class) DrawingDTO drawingUpdate,
            Principal principal) {
        String username = principal.getName();
        Drawing drawing = drawingService.updateDrawing(id, drawingUpdate, username);
        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    @Operation(summary = "Delete specific drawing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrawing(@PathVariable Long id, Authentication authentication){
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        drawingService.deleteDrawing(id, username, isAdmin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/me")
    public ResponseEntity<List<DrawingRoleDTO>> getUserDrawings(Principal principal) {
        String username = principal.getName();
        return new ResponseEntity<>(drawingService.getUserDrawings(username), HttpStatus.OK);
    }



    @Operation(
            summary = "Give user permission to drawing",
            description = "Create new relation between user-drawing. " +
                    "Assign role which specify what user can do with this drawing"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Drawing.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @PostMapping("/{id}/user")
    public ResponseEntity<?> addUserToDrawing(@PathVariable Long id,
                                              @RequestBody @Valid AddUserToDrawingDTO addUserToDrawingDTO,
                                              Principal principal
    ){
        String username = principal.getName();
        UserDrawingRole relation = drawingService.addUserToDrawing(id, addUserToDrawingDTO, username);
        return new ResponseEntity<>(relation, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all users assigned to specific drawing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserRoleDTO.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @GetMapping("/{id}/user")
    public ResponseEntity<List<UserRoleDTO>> getUsersFromDrawing(@PathVariable Long id){
        return new ResponseEntity<>(drawingService.getDrawingUsers(id), HttpStatus.OK);
    }

    @Operation(summary = "Get specific user assigned to specific drawing")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
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
            summary = "Update role in UserDrawingRole for specific relation"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserRoleDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @PutMapping("/{id}/user/{userId}")
    public ResponseEntity<?> updateUserDrawingRole(
            @Parameter(description = "ID of drawing")
            @PathVariable Long id,
            @Parameter(description = "ID of user")
            @PathVariable Long userId,
            @RequestBody @Valid UpdateUserDrawingRoleDTO updateUserDrawingRoleDTO,
            Principal principal
    ){
        String username = principal.getName();
        return new ResponseEntity<>(
                drawingService.updateUserDrawingRole(userId, id, updateUserDrawingRoleDTO, username),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete specific UserDrawingRole")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteUserFromDrawing(
            @Parameter(description = "ID of drawing")
            @PathVariable Long id,
            @Parameter(description = "ID of user")
            @PathVariable Long userId,
            Principal principal
    ){
        String username = principal.getName();
        drawingService.deleteUserDrawingRole(id, userId, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
