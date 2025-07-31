package com.best_store.right_bite.notification.data;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import org.springframework.lang.Nullable;

public sealed interface BaseNotification<T extends NotificationData>
        permits TypedNotification {
    NotificationType type();

    NotificationChannel channel();

    String to();

    @Nullable
    String subject();

    T data();
}
