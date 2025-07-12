package com.best_store.right_bite.dto.notification;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationDataKey;
import com.best_store.right_bite.constant.notification.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.util.Map;

@Schema(description = "Base interface for all notification models. Provides common fields for sending notifications across various channels.")
public interface BaseNotification {

    @Schema(
            description = "Type of the notification, defined by enum.",
            examples = {"REGISTRATION", "RESET_PASSWORD"}
    )
    NotificationType type();

    @Schema(
            description = "Delivery channel for the notification, defined by enum.",
            examples = {"EMAIL", "SMS", "PUSH"}
    )
    NotificationChannel channel();

    @Schema(
            description = "Recipient address. Can be an email, phone number, device token, etc.",
            example = "user@example.com"
    )
    String to();

    @Schema(
            description = "Optional subject of the notification, typically used for email."
    )
    @Nullable
    String subject();

    @Schema(
            description = "Optional key-value map containing template data or dynamic fields for rendering the notification."
    )
    @Nullable
    Map<NotificationDataKey, Object> data();
}
