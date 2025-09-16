package com.best_store.right_bite.dto.notification;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;

public record BuilderInfo(
        NotificationType notificationType,
        NotificationChannel notificationChannel,
        String className
) {
}
