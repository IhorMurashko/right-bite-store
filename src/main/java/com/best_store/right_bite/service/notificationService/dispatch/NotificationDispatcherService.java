package com.best_store.right_bite.service.notificationService.dispatch;

import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

public interface NotificationDispatcherService {
    void send(@NonNull @Valid BaseNotification<? extends ContentPayload> notification);
    boolean canSendNotification(@NonNull @Valid BaseNotification<? extends ContentPayload> notification);
}
