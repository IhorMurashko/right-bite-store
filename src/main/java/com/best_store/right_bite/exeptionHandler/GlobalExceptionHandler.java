package com.best_store.right_bite.exeptionHandler;

import com.best_store.right_bite.dto.exception.ErrorResponseDto;
import com.best_store.right_bite.exception.auth.CredentialsException;
import com.best_store.right_bite.exception.auth.InvalidTokenException;
import com.best_store.right_bite.exception.auth.RefreshTokenAccessException;
import com.best_store.right_bite.exception.auth.UserAccountIsNotAvailableException;
import com.best_store.right_bite.exception.role.RoleNotFoundException;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.util.excetion.ErrorResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRoleNotFoundException(RoleNotFoundException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(CredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleCredentialsException(CredentialsException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RefreshTokenAccessException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAccountIsNotAvailableException(RefreshTokenAccessException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAccountIsNotAvailableException(InvalidTokenException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UserAccountIsNotAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAccountIsNotAvailableException(UserAccountIsNotAvailableException ex, HttpServletRequest request) {
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage()
                ))
                .toList();

        return ErrorResponseBuilder.buildValidationErrorResponse(errors, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        List<Map<String, String>> errors = ex.getConstraintViolations().stream()
                .map(violation -> Map.of(
                        "field", ErrorResponseBuilder.extractFieldName(violation.getPropertyPath().toString()),
                        "message", violation.getMessage()
                ))
                .toList();

        return ErrorResponseBuilder.buildValidationErrorResponse(errors, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAll(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error occurred", ex);
        return ErrorResponseBuilder.buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
