package com.best_store.right_bite.dto.notification.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.EMAIL_MESSAGE;
import static com.best_store.right_bite.constant.constraint.UserFieldsConstraint.EMAIL_PATTERN;

public record NotificationSubscriptionRequestDto(
        @NotNull
        @NotBlank
        @Pattern(regexp = EMAIL_PATTERN, message = EMAIL_MESSAGE)
        String email
) implements Serializable {
}
