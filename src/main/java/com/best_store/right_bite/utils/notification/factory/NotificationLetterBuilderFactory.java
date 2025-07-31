package com.best_store.right_bite.utils.notification.factory;


import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.utils.notification.contentBuilder.NotificationContentBuilder;

/**
 * Factory interface for retrieving {@link NotificationContentBuilder} implementations.
 *
 * <p>Provides a method to resolve the appropriate builder based on the
 * notification type and delivery channel.
 *
 * <p>Intended to streamline the process of selecting the correct
 * {@link NotificationContentBuilder} implementation for constructing
 * notification content.
 *
 * <p>Throws an exception if no suitable builder is found for the specified
 * type and channel.
 *
 * @see NotificationContentBuilder
 * @see NotificationType
 * @see NotificationChannel
 *
 * @author Ihor Murashko
 */
@FunctionalInterface
public interface NotificationLetterBuilderFactory {

    NotificationContentBuilder getNotificationBuilder(NotificationType type,
                                                      NotificationChannel channel);
}
