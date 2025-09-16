package com.best_store.right_bite.notification.data.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordContentPayload(
        @NotNull @NotBlank
        String newPassword
) implements ContentPayload {
}