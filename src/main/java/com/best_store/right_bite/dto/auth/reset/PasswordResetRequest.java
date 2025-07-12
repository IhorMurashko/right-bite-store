package com.best_store.right_bite.dto.auth.reset;

import jakarta.validation.constraints.Email;
import org.springframework.lang.NonNull;

import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.EMAIL_MESSAGE;
import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.EMAIL_PATTERN;

public record PasswordResetRequest(
        @NonNull
        @Email(regexp = EMAIL_PATTERN,
                message = EMAIL_MESSAGE)
        String email) {
}
