package com.best_store.right_bite.utils.excetion;

import com.best_store.right_bite.dto.exception.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
@Slf4j
public class ErrorResponseBuilder {

    public ResponseEntity<ErrorResponseDto> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        log.warn("catch exception: {} with message: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(status)
                .body(new ErrorResponseDto(LocalDateTime.now(), status.value(),
                        status.getReasonPhrase(), ex.getMessage(), request.getRequestURI()));

    }

    public ResponseEntity<Object> buildValidationErrorResponse(List<Map<String, String>> errors, HttpStatus status, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getRequestURI());
        body.put("errors", errors);
        return ResponseEntity.status(status).body(body);
    }

    public String extractFieldName(String fullPath) {
        int dotIndex = fullPath.lastIndexOf('.');
        return (dotIndex >= 0) ? fullPath.substring(dotIndex + 1) : fullPath;
    }

}
