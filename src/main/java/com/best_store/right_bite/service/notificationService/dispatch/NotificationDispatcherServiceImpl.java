package com.best_store.right_bite.service.notificationService.dispatch;

import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.notification.sender.NotificationSender;
import com.best_store.right_bite.utils.notification.cache.builders.NotificationBuildersCache;
import com.best_store.right_bite.utils.notification.cache.senders.SenderFactory;
import com.best_store.right_bite.utils.notification.contentBuilder.NotificationContentBuilder;
import com.best_store.right_bite.utils.notification.contentProcessor.NotificationContentProcessor;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class NotificationDispatcherServiceImpl implements NotificationDispatcherService {

    private final NotificationBuildersCache notificationBuildersCache;
    private final NotificationContentProcessor contentProcessor;
    private final SenderFactory senderFactory;

    @Override
    public void send(@NotNull @Valid BaseNotification<? extends ContentPayload> notification) {
        NotificationContentBuilder<? extends ContentPayload> builder = notificationBuildersCache.findBuilder(notification);
        String content = contentProcessor.processContent(builder, notification);
        NotificationSender sender = senderFactory.getNotificationSender(notification.channel());
        sender.send(notification, content);
    }

    @Override
    public boolean canSendNotification(@NotNull @Valid BaseNotification<? extends ContentPayload> notification) {
        return notificationBuildersCache.hasBuilder(notification);
    }
}