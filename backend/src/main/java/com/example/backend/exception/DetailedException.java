package com.example.backend.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
public abstract class DetailedException extends RuntimeException {
    private final Map<String, Object> criteria;

    public DetailedException(String message, Map<String, Object> criteria) {
        super(message);
        this.criteria = criteria;
    }

    public DetailedException(String message) {
        super(message);
        this.criteria = null;
    }
}
