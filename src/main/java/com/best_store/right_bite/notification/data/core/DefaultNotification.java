package com.best_store.right_bite.notification.data.core;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.notification.data.payload.ContentPayload;

import java.util.List;

public record DefaultNotification(
        NotificationType type,
        NotificationChannel channel,
        List<String> recipients,
        String emailSubject,
        String bodyTitle,
        ContentPayload data
) implements BaseNotification<ContentPayload> {
}
