package com.example.backend.controller;

import com.example.backend.exception.ApiExceptionResponse;
import com.example.backend.model.User;
import com.example.backend.model.UserStatistics;
import com.example.backend.service.UserStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/user/stats")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Statistics")
@SecurityRequirement(name = "bearerAuth")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    public UserStatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    @Operation(
            summary = "Get specific user statistics",
            description = "Can be used only by any user to get any user statistics"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got user statistics",
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
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserStatistics> getUserStatisticsByUserId(
            @Parameter(description = "Id of the user to get his statistics")
            @PathVariable Long userId
    ) {
        UserStatistics userStatistics = userStatisticsService.getUserStatisticsByUserId(userId);
        return new ResponseEntity<>(userStatistics, HttpStatus.OK);
    }

    @Operation(
            summary = "Increase click count stat",
            description = "Can be used by any user to increase their click count stat"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully added request to the queue",
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
                    description = "Need JWT token",
                    content = @Content()
            )
    })
    @PostMapping("")
    public ResponseEntity<?> clickUserStatistics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userStatisticsService.clickToQueue(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

