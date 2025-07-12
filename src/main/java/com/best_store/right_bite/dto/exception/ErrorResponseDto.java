package com.best_store.right_bite.dto.exception;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ErrorResponseDto(LocalDateTime timestamp,
                               int status,
                               String error,
                               String message,
                               String path) implements Serializable {
}
