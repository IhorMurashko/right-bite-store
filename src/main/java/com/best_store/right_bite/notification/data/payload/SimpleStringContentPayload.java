package com.best_store.right_bite.notification.data.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SimpleStringContentPayload(
        @NotNull
        @NotBlank
        String content
) implements ContentPayload {
}