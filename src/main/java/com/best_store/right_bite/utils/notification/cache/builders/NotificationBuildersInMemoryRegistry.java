package com.best_store.right_bite.utils.notification.cache.builders;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.dto.notification.BuilderInfo;
import com.best_store.right_bite.exception.messageProvider.NotificationExceptionMP;
import com.best_store.right_bite.exception.exceptions.notification.NotificationBuilderWasNotFoundException;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.utils.notification.contentBuilder.NotificationContentBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationBuildersInMemoryRegistry implements NotificationBuildersCache {

    private final ApplicationContext context;
    private Map<String, NotificationContentBuilder<? extends ContentPayload>> buildersCacheHolder;

    @Override
    public NotificationContentBuilder<? extends ContentPayload> findBuilder(@NonNull BaseNotification<? extends ContentPayload> notification) {
        String builderKey = generateBuilderKey(
                notification.type(), notification.channel());
        NotificationContentBuilder<? extends ContentPayload> builder = buildersCacheHolder.get(builderKey);
        if (builder == null) {
            log.warn("Builder was not found for type: {} and channel: {}", notification.type(), notification.channel());
            throw new NotificationBuilderWasNotFoundException(
                    String.format(NotificationExceptionMP.NOTIFICATION_BUILDER_WAS_NOT_FOUND,
                            notification.type(), notification.channel()));
        }
        return builder;
    }

    @Override
    public boolean hasBuilder(@NonNull BaseNotification<? extends ContentPayload> notificationContentBuilder) {
        String key = generateBuilderKey(notificationContentBuilder.type(), notificationContentBuilder.channel());
        try {
            return buildersCacheHolder.containsKey(key);
        } catch (NotificationBuilderWasNotFoundException ex) {
            log.warn("Notification builder was not found for type: {} and channel: {}",
                    notificationContentBuilder.type(), notificationContentBuilder.channel());
            return false;
        }
    }

    @Override
    public List<BuilderInfo> getAllBuilders() {
        return context.getBeansOfType(NotificationContentBuilder.class)
                .values()
                .stream()
                .map(builder -> new BuilderInfo(
                        builder.getNotificationType(),
                        builder.getNotificationChannel(),
                        builder.getClass().getSimpleName()
                )).collect(Collectors.toList());
    }

    @Override
    public String generateBuilderKey(NotificationType notificationType, NotificationChannel notificationChannel) {
        return notificationType + "_" + notificationChannel;
    }

    @PostConstruct
    private void fillBuildersHolder() {
            this.buildersCacheHolder = new ConcurrentHashMap<>();
        context.getBeansOfType(NotificationContentBuilder.class)
                .values()
                .stream()
                .filter(Objects::nonNull)
                .forEach(builder ->
                        this.buildersCacheHolder.computeIfAbsent(generateBuilderKey(
                                builder.getNotificationType(), builder.getNotificationChannel()), key -> builder));
    }
}