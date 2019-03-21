package com.cloudvisor.beerstore.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
@RequiredArgsConstructor
public class ErrorResponse {

    private final int statusCode;
    private final String reason;
    private final List<ApiError> errors;

    static ErrorResponse ofErrors(HttpStatus status, List<ApiError> errors) {
        return new ErrorResponse(status.value(), status.getReasonPhrase(), errors);
    }

    static ErrorResponse of(HttpStatus status, ApiError error) {
        return ofErrors(status, Collections.singletonList(error));
    }

    @JsonAutoDetect(fieldVisibility = ANY)
    @RequiredArgsConstructor
    static class ApiError {

        private final String code;
        private final String message;

    }
}