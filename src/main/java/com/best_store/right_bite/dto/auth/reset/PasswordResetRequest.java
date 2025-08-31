package com.best_store.right_bite.dto.auth.reset;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import org.springframework.lang.NonNull;

import static com.best_store.right_bite.constant.constraint.user.UserFieldsConstraint.EMAIL_MESSAGE;
import static com.best_store.right_bite.constant.constraint.user.UserFieldsConstraint.EMAIL_PATTERN;

@Schema(description = "reset password object")
public record PasswordResetRequest(
        @Schema(description = "email",
                nullable = false,
                pattern = EMAIL_PATTERN)
        @NonNull
        @Email(regexp = EMAIL_PATTERN,
                message = EMAIL_MESSAGE)
        String email) {
}
