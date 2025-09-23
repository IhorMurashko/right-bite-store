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

/**
 * Utility class for building structured error responses for exception handling.
 * Provides methods to create standardized error responses and extract field names.
 * <p>
 * This class is commonly used in global exception handlers to respond to client requests with appropriate error details.
 * </p>
 *
 * @author Ihor Murashko
 */
@UtilityClass
@Slf4j
public class ErrorResponseBuilder {

    /**
     * Constructs a detailed error response encapsulated in a {@link ResponseEntity}.
     * This utility method is used to generate standardized error responses for exceptions.
     *
     * @param ex      the exception that occurred
     * @param status  the HTTP status to be sent in the response
     * @param request the incoming HTTP request causing the exception
     * @return a {@link ResponseEntity} containing the error details in an {@link ErrorResponseDto} object
     * @author Ihor Murashko
     */
    public ResponseEntity<ErrorResponseDto> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
        log.warn("catch exception: {} with message: {}", ex.getClass().getSimpleName(), ex.getMessage());
        return ResponseEntity.status(status)
                .body(new ErrorResponseDto(LocalDateTime.now(), status.value(),
                        status.getReasonPhrase(), ex.getMessage(), request.getRequestURI()));

    }

    /**
     * Builds and returns a structured validation error response with detailed error information.
     * This method is typically used to construct responses for client-side validation errors.
     *
     * @param errors A list of error details represented as maps with "field" and "message" keys.
     * @param status The HTTP status to be returned in the response.
     * @param request The HTTP request object, used to extract request-specific information.
     * @return A {@link ResponseEntity} object containing validation error details and metadata.
     *
     * @author Ihor Murashko
     */
    public ResponseEntity<Object> buildValidationErrorResponse(List<Map<String, String>> errors, HttpStatus status, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("path", request.getRequestURI());
        body.put("errors", errors);
        return ResponseEntity.status(status).body(body);
    }

    /**
     * Extracts the field name from a fully qualified property path.
     * If the path contains a period ('.'), the part after the last period is returned.
     * If no period is found, the original path is returned as the field name.
     *
     * @param fullPath the fully qualified path of the property, e.g., "object.field.name".
     * @return the extracted field name, e.g., "name", or the original path if no period exists.
     * @author Ihor Murashko
     */
    public String extractFieldName(String fullPath) {
        int dotIndex = fullPath.lastIndexOf('.');
        return (dotIndex >= 0) ? fullPath.substring(dotIndex + 1) : fullPath;
    }
}