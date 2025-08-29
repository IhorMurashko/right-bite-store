package com.best_store.right_bite.notification;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.NotificationPayload;
import org.springframework.lang.NonNull;

/**
 * Interface for sending notifications across different channels.
 *
 * This interface defines methods for retrieving the notification channel
 * and sending notification messages. Implementations of this interface
 * are responsible for handling specific channels (e.g., EMAIL, SMS, PUSH)
 * and ensuring delivery of the notification's content to the intended recipient.
 *
 * @author Ihor Murashko
 */
public interface NotificationSender {

    NotificationChannel getNotificationChannel();
    void send(@NonNull BaseNotification <? extends NotificationPayload> notification, @NonNull String content);
}