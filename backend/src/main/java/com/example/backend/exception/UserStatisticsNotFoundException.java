package com.example.backend.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class UserStatisticsNotFoundException extends DetailedException{
    public UserStatisticsNotFoundException(Map<String, Object> criteria) {
        super("UserStatistics not found", criteria);
    }

    public UserStatisticsNotFoundException() { super("UserStatistics not found"); }
}
