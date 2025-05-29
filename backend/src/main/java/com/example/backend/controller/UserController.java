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
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "User")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Get specific user",
            description = "Can be used only by admin to get specific user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token with admin security role",
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

    @Operation(
            summary = "Get summary of the user",
            description = "Can be used only by any user to get summary of specific user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got summary",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"id\": \"1\", \"name\": \"Jamal\", }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such user",
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
    @GetMapping("/{id}/summary")
    public ResponseEntity<UserDTO> getUserSummary(
            @Parameter(description = "ID of user to be fetched")
            @PathVariable Long id
    ){
        UserDTO userDTO = userService.getUserSummary(id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get summary of the user which called request",
            description = "Can be used by any user to get his summary"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got summary",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{ \"id\": \"1\", \"name\": \"Jamal\", }"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "There is no such user",
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
    @GetMapping("/summary")
    public ResponseEntity<UserDTO> getMySummary(
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDTO userDTO = userService.getUserSummary(username);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @Operation(
            summary = "Get all users",
            description = "Can be used only by admin to get list of users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got list of users",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Need JWT token with admin security role",
                    content = @Content()
            )
    })
    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @Operation(
            summary = "Update specific user",
            description = "Update only provided fields of specific user, returns jwt token."
            + "Users can update only their data. Admin can edit any user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated user",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No such user found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiExceptionResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "New credentials are already taken",
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
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<AuthResponseDTO> updateUser(
            @Parameter(description = "ID of the user to update")
            @PathVariable Long id,
            @RequestBody @Validated(UserDTO.Update.class) UserDTO userDTO
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        AuthResponseDTO update = userService.updateUser(id, userDTO, username, isAdmin);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @Operation(
            summary = "Delete specific user",
            description = "User can delete himself. Admin can delete any user"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted user",
                    content = @Content()
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No such user found",
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
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "ID of the user to delete")
            @PathVariable Long id
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        userService.deleteUser(id, username, isAdmin);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
