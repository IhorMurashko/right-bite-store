package com.best_store.right_bite.service.notificationService;

import com.best_store.right_bite.dto.notification.BaseNotification;
import com.best_store.right_bite.notification.NotificationSender;
import com.best_store.right_bite.notification.sender.factory.SenderFactory;
import com.best_store.right_bite.util.notification.contentBuilder.NotificationContentBuilder;
import com.best_store.right_bite.util.notification.factory.NotificationLetterBuilderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link NotificationDispatcherService} interface responsible for managing the
 * notification delivery process.
 *
 * <p>This service coordinates the resolution of the appropriate notification sender and
 * content builder for sending notifications through the specified channel with the
 * specified notification type.
 *
 * <p>Relies on {@link SenderFactory} to retrieve the correct {@link NotificationSender},
 * and utilizes {@link NotificationLetterBuilderFactory} to construct the notification content
 * before delivering it using the resolved sender.
 *
 * <p>Supported notification channels and types are determined by the underlying factories
 * and their respective configurations.
 *
 * <p>Logging is integrated to capture actions and errors at various stages of the dispatch process.
 *
 * @author Ihor Murashko
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationDispatcherServiceImpl implements NotificationDispatcherService {


    private final SenderFactory senderFactory;
    private final NotificationLetterBuilderFactory letterBuilderFactory;

    @Override
    public void send(@NonNull BaseNotification notification) {
        NotificationSender sender = senderFactory.getSender(notification.channel());
        NotificationContentBuilder notificationContentBuilder = letterBuilderFactory
                .getNotificationBuilder(notification.type(), notification.channel());
        String letter = notificationContentBuilder.build(notification);
        sender.send(notification, letter);
    }
}