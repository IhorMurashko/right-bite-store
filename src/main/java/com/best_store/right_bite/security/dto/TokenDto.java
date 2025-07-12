package com.best_store.right_bite.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

@Schema(name = "token dto")
public record TokenDto(
        @Schema(description = "access token")
        @NonNull
        @NotBlank
        String accessToken,
        @Schema(description = "refresh token")
        @NonNull
        @NotBlank
        String refreshToken) {
}
