package com.best_store.right_bite.utils.notification.contentBuilder;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.notification.data.BaseNotification;
import com.best_store.right_bite.notification.data.NotificationData;
import org.springframework.lang.NonNull;

/**
 * Defines a contract for building notification content based on specific notification types
 * and delivery channels.
 *
 * <p>Implementations of this interface are responsible for constructing the content
 * of notifications that can be delivered via various channels (e.g., Email, SMS, Push)
 * and for specific notification types (e.g., REGISTRATION, RESET_PASSWORD).
 * </p>
 *
 * <p>Typical usage involves:
 * <ul>
 *   <li>Providing a way to build notification content using the {@link #build(BaseNotification)} method.</li>
 *   <li>Defining the notification type supported by the implementation via {@link #getNotificationType()}.</li>
 *   <li>Defining the notification channel supported by the implementation via {@link #getNotificationChannel()}.</li>
 * </ul>
 * </p>
 *
 * <p>This interface is intended to be used in coordination with a factory or service
 * that selects the appropriate {@code NotificationContentBuilder} implementation
 * based on notification type and channel.</p>
 *
 * <p>This interface ensures flexibility and separation of concerns
 * in creating notification systems.</p>
 *
 * @author Ihor Murashko
 */
public interface NotificationContentBuilder {

    String build(@NonNull BaseNotification<? extends NotificationData> notification);

    NotificationType getNotificationType();

    NotificationChannel getNotificationChannel();

}
