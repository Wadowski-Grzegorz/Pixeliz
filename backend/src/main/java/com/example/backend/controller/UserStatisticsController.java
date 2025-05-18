package com.example.backend.controller;

import com.example.backend.model.UserStatistics;
import com.example.backend.service.UserStatisticsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.security.Principal;

@RestController
@RequestMapping("/api/user/{userId}/stats")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Statistics")
@SecurityRequirement(name = "bearerAuth")
public class UserStatisticsController {

    private final UserStatisticsService userStatisticsService;

    public UserStatisticsController(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    @GetMapping("")
    public ResponseEntity<UserStatistics> getUserStatisticsByUserId(@PathVariable Long userId) {
        UserStatistics userStatistics = userStatisticsService.getUserStatisticsByUserId(userId);
        return new ResponseEntity<>(userStatistics, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> clickUserStatistics(@PathVariable Long userId, Principal principal) {
        String username = principal.getName();
        userStatisticsService.clickToQueue(userId, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

