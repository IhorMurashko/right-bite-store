package com.best_store.right_bite.service.notificationService.dispatch;

import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface NotificationDispatcherService {
    void send(@NotNull @Valid BaseNotification<? extends ContentPayload> notification);

    boolean canSendNotification(@NotNull @Valid BaseNotification<? extends ContentPayload> notification);
}
