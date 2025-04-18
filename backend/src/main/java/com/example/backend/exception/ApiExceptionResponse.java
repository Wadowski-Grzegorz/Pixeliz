package com.example.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionResponse {
    private final String error;
    private final int status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Map<String, Object> details;

    public ApiExceptionResponse(String error, int status) {
        this.error = error;
        this.status = status;
        this.details = null;
    }
}
