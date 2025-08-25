package com.best_store.right_bite.notification.data.core;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.notification.data.payload.NotificationPayload;
import org.springframework.lang.Nullable;

import java.util.List;

public interface BaseNotification<T extends NotificationPayload> {
    NotificationType type();

    NotificationChannel channel();

    List<String> recipients();

    @Nullable
    String subject();

    T data();
}
