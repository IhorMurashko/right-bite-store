package com.best_store.right_bite.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

/**
 * Represents a data transfer object (DTO) for JWT tokens, including access and refresh tokens.
 * <p>
 * Used throughout the application for token operations, such as generating,
 * refreshing, and validating JWT tokens.
 *
 * @param accessToken  the access token for authenticating requests; must be non-null and not blank.
 * @param refreshToken the refresh token for renewing access tokens; must be non-null and not blank.
 */
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