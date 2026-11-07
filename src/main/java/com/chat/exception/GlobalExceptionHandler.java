package com.chat.exception;

import com.chat.dto.ApiResponse;
import com.chat.util.LoggerUtil;

import org.slf4j.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerUtil.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(
            IllegalArgumentException ex
    ) {
        log.warn("Invalid request: {}", ex.getMessage());

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                HttpStatus.BAD_REQUEST,
                null
        );

        return ResponseEntity
                .status(response.getHttpCode())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex
    ) {
        log.error("Unexpected application error", ex);

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null
        );

        return ResponseEntity
                .status(response.getHttpCode())
                .body(response);
    }
}
