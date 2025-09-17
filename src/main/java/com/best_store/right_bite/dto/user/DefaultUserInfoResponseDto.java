package com.best_store.right_bite.dto.user;

import com.best_store.right_bite.model.role.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO representing the full user profile returned in responses.
 *
 * <p>Includes both identity information (ID, email) and profile metadata (names,
 * contact, address, account status, etc.).</p>
 *
 * <p>Implements {@link BaseUserInfo} for compatibility with higher-level abstractions.</p>
 *
 * <p>Used for responses in secured user-related endpoints.</p>
 */
@Schema(description = "User information DTO for responses")
public record DefaultUserInfoResponseDto(
        @Schema(description = "Unique user ID", example = "42")
        long id,

        @Schema(description = "User's email address", example = "user@example.com")
        String email,

        @Schema(description = "Indicates whether the account is not expired", example = "true")
        boolean isAccountNonExpired,

        @Schema(description = "Indicates whether the account is not locked", example = "true")
        boolean isAccountNonLocked,

        @Schema(description = "Indicates whether the credentials are not expired", example = "true")
        boolean isCredentialsNonExpired,

        @Schema(description = "Indicates whether the account is enabled", example = "true")
        boolean isEnabled,

        @Schema(description = "Timestamp when the user was created", example = "2025-07-31T14:48:00")
        LocalDateTime createdAt,

        @Schema(description = "User's first name", example = "John")
        String firstName,

        @Schema(description = "User's last name", example = "Doe")
        String lastName,

        @Schema(description = "URL to user's profile image", example = "https://example.com/images/user.png")
        String imageUrl,

        @Schema(description = "User's phone number", example = "+123456789")
        String phoneNumber,

        @Schema(description = "Country where user lives", example = "Slovenia")
        String country,

        @Schema(description = "City where user lives", example = "Ljubljana")
        String city,

        @Schema(description = "Street name", example = "Trubarjeva")
        String streetName,

        @Schema(description = "House number", example = "12A")
        String houseNumber,

        @Schema(description = "ZIP code", example = "1000")
        String zipCode,

        @Schema(description = "User's roles", examples = {"ROLE_USER", "ROLE_ADMIN"})
        Set<String> roles) implements Serializable, BaseUserInfo {
}