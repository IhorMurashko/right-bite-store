package com.best_store.right_bite.dto.notification;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationDataKey;
import com.best_store.right_bite.constant.notification.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.Map;

@Schema(
        name = "DefaultNotification",
        description = "Standard notification data.sql structure used to send messages via different channels."
)
public record DefaultNotification(

        @Schema(
                description = "Type of the notification, defined by enum.",
                example = "REGISTRATION"
        )
        NotificationType type,

        @Schema(
                description = "Notification delivery channel.",
                example = "EMAIL"
        )
        NotificationChannel channel,

        @Schema(
                description = "Recipient identifier: email, phone number, device token, etc.",
                example = "user@example.com"
        )
        String to,

        @Schema(
                description = "Optional subject for the notification. Mainly used for email messages.",
                example = "Welcome to our service!"
        )
        @Nullable
        String subject,

        @Schema(
                description = "Optional map of dynamic data.sql used to fill notification templates."
        )
        @Nullable
        Map<NotificationDataKey, Object> data

) implements Serializable, BaseNotification {
}
