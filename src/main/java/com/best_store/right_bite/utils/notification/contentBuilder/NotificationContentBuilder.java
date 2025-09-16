package com.best_store.right_bite.utils.notification.contentBuilder;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import org.springframework.lang.NonNull;

public interface NotificationContentBuilder<T extends ContentPayload> {

    NotificationType getNotificationType();

    NotificationChannel getNotificationChannel();

    String build(@NonNull BaseNotification<? extends ContentPayload> notification);
}