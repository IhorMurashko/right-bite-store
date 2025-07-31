package com.best_store.right_bite.notification.data;

import jakarta.validation.constraints.NotBlank;

public record SimpleTextNotification(
        @NotBlank String content
) implements NotificationData {
}
