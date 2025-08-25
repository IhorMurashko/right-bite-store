package com.best_store.right_bite.service.notificationService;

import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.NotificationPayload;
import org.springframework.lang.NonNull;

/**
 * Service responsible for coordinating notification delivery.
 *
 * <p>Resolves appropriate content builders and sender implementations
 * based on the provided notification channel.
 *
 * @author Ihor Murashko
 */
@FunctionalInterface
public interface NotificationDispatcherService {
    void send(@NonNull BaseNotification<? extends NotificationPayload> notification);
}
