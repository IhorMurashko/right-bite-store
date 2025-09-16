package com.best_store.right_bite.utils.notification.cache.senders;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.notification.sender.NotificationSender;
import org.springframework.lang.NonNull;

@FunctionalInterface
public interface SenderFactory {
    NotificationSender getNotificationSender(@NonNull NotificationChannel notificationChannel);
}
