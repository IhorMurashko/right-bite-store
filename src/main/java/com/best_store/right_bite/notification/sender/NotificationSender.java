package com.best_store.right_bite.notification.sender;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import org.springframework.lang.NonNull;

public interface NotificationSender {
    NotificationChannel getNotificationChannel();
    void send(@NonNull BaseNotification<? extends ContentPayload> notification, @NonNull String content);
}
