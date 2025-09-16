package com.best_store.right_bite.utils.notification.cache.builders;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.dto.notification.BuilderInfo;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.utils.notification.contentBuilder.NotificationContentBuilder;
import org.springframework.lang.NonNull;

import java.util.List;

public interface NotificationBuildersCache {

    boolean hasBuilder(@NonNull BaseNotification<? extends ContentPayload> notificationContentBuilder);

    List<BuilderInfo> getAllBuilders();

    NotificationContentBuilder<? extends ContentPayload> findBuilder(@NonNull BaseNotification<? extends ContentPayload> notification);

    String generateBuilderKey(NotificationType notificationType, NotificationChannel notificationChannel);
}
