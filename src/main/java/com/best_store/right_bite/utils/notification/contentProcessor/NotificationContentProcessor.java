package com.best_store.right_bite.utils.notification.contentProcessor;

import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.utils.notification.contentBuilder.NotificationContentBuilder;
import org.springframework.lang.NonNull;

@FunctionalInterface
public interface NotificationContentProcessor {
    String processContent(@NonNull NotificationContentBuilder<? extends ContentPayload> builder,
                          @NonNull BaseNotification<? extends ContentPayload> notification);
}