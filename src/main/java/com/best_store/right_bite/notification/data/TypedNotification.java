package com.best_store.right_bite.notification.data;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import org.springframework.lang.Nullable;

import java.io.Serializable;

public record TypedNotification<T extends NotificationData>(

        NotificationType type,
        NotificationChannel channel,
        String to,
        @Nullable
        String subject,
        T data
) implements Serializable, BaseNotification<T> {
}
