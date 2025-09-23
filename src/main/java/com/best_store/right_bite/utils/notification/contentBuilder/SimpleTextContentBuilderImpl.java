package com.best_store.right_bite.utils.notification.contentBuilder;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.constant.notification.holder.ContextVariablesHolder;
import com.best_store.right_bite.constant.notification.holder.FragmentsReferencesHolder;
import com.best_store.right_bite.exception.messageProvider.NotificationExceptionMP;
import com.best_store.right_bite.exception.exceptions.notification.WrongNotificationDataException;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.notification.data.payload.SimpleStringContentPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleTextContentBuilderImpl implements NotificationContentBuilder<SimpleStringContentPayload> {
    private final NotificationType notificationType;
    private final NotificationChannel notificationChannel;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public SimpleTextContentBuilderImpl(SpringTemplateEngine templateEngine) {
        this.notificationType = NotificationType.SIMPLE_STRING_NOTIFICATION;
        this.notificationChannel = NotificationChannel.EMAIL;
        this.templateEngine = templateEngine;
    }

    @Override
    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    @Override
    public NotificationChannel getNotificationChannel() {
        return this.notificationChannel;
    }

    @Override
    public String build(@NonNull BaseNotification<? extends ContentPayload> notification) {
        if (notification.data() == null) {
            log.error("Notification data is null");
            throw new WrongNotificationDataException(NotificationExceptionMP.EMPTY_NOTIFICATION_DATA);
        }
        if (notification.data().getClass() != SimpleStringContentPayload.class) {
            log.error("Wrong notification data type: {}", notification.data().getClass().getSimpleName());
            throw new WrongNotificationDataException(NotificationExceptionMP.WRONG_NOTIFICATION_DATA_TYPE);
        }
        log.debug("Building notification content for channel: {}", notification.channel());
        log.debug("Notification type: {}", notification.type());


        SimpleStringContentPayload payload = (SimpleStringContentPayload) notification.data();
        Context context = new Context();
        context.setVariable(ContextVariablesHolder.TEMPLATE, FragmentsReferencesHolder.SIMPLE_TEXT);
        context.setVariable(ContextVariablesHolder.SIMPLE_TEXT, payload.content());
        context.setVariable(ContextVariablesHolder.SUBJECT, notification.bodyTitle());
        log.debug("Notification content was built successfully");
        return templateEngine.process(FragmentsReferencesHolder.BASE_NOTIFICATION, context);
    }
}
