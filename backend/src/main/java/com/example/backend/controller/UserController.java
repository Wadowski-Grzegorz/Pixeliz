package com.example.backend.controller;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.exception.ApiExceptionResponse;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get specific user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
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
    public ResponseEntity<User> getUser(
            @Parameter(description = "ID of user to be fetched")
            @PathVariable Long id){
        User user = userService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Get all users")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    content = @Content()
            )
    })
    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update specific user",
            description = "Update only provided fields of specific user, returns jwt token"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
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
                    responseCode = "409",
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
    public ResponseEntity<AuthResponseDTO> updateUser(
            @Parameter(description = "ID of role to be fetched")
            @PathVariable Long id,
            @RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO){
        AuthResponseDTO update = userService.updateUser(id, userDTO);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Operation(summary = "Delete specific user")
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
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
