package com.best_store.right_bite.dto.user.update;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.lang.Nullable;

import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.*;

/**
 * Request DTO for partial user profile updates.
 * <p>
 * Each field is optional. Only non-null values will be processed and validated.
 * Useful for PATCH operations on user entities.
 * </p>
 *
 * <p>Validation is enforced via regex patterns defined in constants.</p>
 *
 * @author Ihor Murashko
 */
@Schema(description = "DTO for user profile update requests. Only non-null fields will be processed and validated.")
@Builder
public record UserUpdateRequestDto(
        @Schema(description = "New email address (must follow standard email format)",
                example = "new.email@example.com")
        @Nullable
        @Pattern(regexp = EMAIL_PATTERN,
                message = EMAIL_MESSAGE)
        String email,

        @Schema(description = "New password (must follow security pattern)", example = "StrongP@ssw0rd!")
        @Nullable
        @Pattern(regexp = PASSWORD_PATTERN,
                message = PASSWORD_MESSAGE)
        String password,

        @Schema(description = "New first name", example = "Jane")
        @Nullable
        @Pattern(regexp = NAME_PATTERN,
                message = NAME_MESSAGE)
        String firstName,

        @Schema(description = "New last name", example = "Smith")
        @Nullable
        @Pattern(regexp = NAME_PATTERN,
                message = NAME_MESSAGE)
        String lastName,

        @Schema(description = "New phone number", example = "38640111222")
        @Nullable
        @Pattern(regexp = PHONE_PATTERN,
                message = PHONE_MESSAGE)
        String phoneNumber,

        @Schema(description = "New country", example = "Ukraine")
        @Nullable
        @Pattern(regexp = ADDRESS_PATTERN,
                message = ADDRESS_MESSAGE)
        String country,

        @Schema(description = "New city", example = "Kiev")
        @Nullable
        @Pattern(regexp = ADDRESS_PATTERN,
                message = ADDRESS_MESSAGE)
        String city,

        @Schema(description = "New street name", example = "Zagaydachnega")
        @Nullable
        @Pattern(regexp = ADDRESS_PATTERN,
                message = ADDRESS_MESSAGE)
        String streetName,

        @Schema(description = "New house number", example = "15B")
        @Nullable
        @Pattern(regexp = HOUSE_NUMBER_PATTERN,
                message = HOUSE_NUMBER_MESSAGE)
        String houseNumber,

        @Schema(description = "New ZIP code", example = "1500")
        @Nullable
        @Pattern(regexp = ZIP_CODE_PATTERN,
                message = ZIP_CODE_MESSAGE)
        String zipCode
) {
}

