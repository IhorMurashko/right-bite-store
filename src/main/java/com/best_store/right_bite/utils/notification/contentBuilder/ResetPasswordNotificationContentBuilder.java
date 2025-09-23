package com.best_store.right_bite.utils.notification.contentBuilder;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.constant.notification.holder.ContextVariablesHolder;
import com.best_store.right_bite.constant.notification.holder.FragmentsReferencesHolder;
import com.best_store.right_bite.constant.notification.holder.letter.ResetPasswordVariablesHolder;
import com.best_store.right_bite.exception.messageProvider.NotificationExceptionMP;
import com.best_store.right_bite.exception.exceptions.notification.WrongNotificationDataException;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.notification.data.payload.ResetPasswordContentPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * A concrete implementation of {@link NotificationContentBuilder} that builds email notification content
 * using the Spring Template Engine.
 *
 * <p>This class is responsible for rendering email templates based on the {@link BaseNotification}
 * object provided. It is pre-configured to handle email channels and "REGISTRATION" notification type.
 * </p>
 *
 * <p>Uses Thymeleaf template engine to create dynamic email content for notifications.
 * </p>
 *
 * <p>It provides methods to:
 * <ul>
 *   <li>Build notification content for email templates.</li>
 *   <li>Fetch the associated {@link NotificationType} and {@link NotificationChannel} of the builder.</li>
 * </ul>
 *
 * <p>Dependencies:
 * <ul>
 *   <li>{@link SpringTemplateEngine}: Used for processing the email templates.</li>
 * </ul>
 * </p>
 *
 * <p>Annotations:
 * <ul>
 *   <li>{@code @Component}: Indicates that this class is a Spring-managed component.</li>
 *   <li>{@code @Slf4j}: Provides logging capabilities.</li>
 * </ul>
 * </p>
 *
 * <p>Preconfigured to work with:
 * <ul>
 *   <li>Notification Type: {@code NotificationType.TEXT_MESSAGE}</li>
 *   <li>Channel: {@code NotificationChannel.EMAIL}</li>
 * </ul>
 * </p>
 *
 * <p>Template Path:
 * <ul>
 *   <li>{@code "notification/email/textMessage"}</li>
 * </ul>
 * </p>
 *
 * @author Ihor Murashko
 */
@Component
@Slf4j
public class ResetPasswordNotificationContentBuilder implements NotificationContentBuilder<ResetPasswordContentPayload> {

    private final NotificationType notificationType;
    private final NotificationChannel notificationChannel;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public ResetPasswordNotificationContentBuilder(SpringTemplateEngine templateEngine) {
        this.notificationType = NotificationType.RESETTING_PASSWORD_NOTIFICATION;
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
        if (notification.data().getClass() != ResetPasswordContentPayload.class) {
            log.error("Wrong notification data type: {}", notification.data().getClass().getSimpleName());
            throw new WrongNotificationDataException(NotificationExceptionMP.WRONG_NOTIFICATION_DATA_TYPE);
        }
        log.debug("Building notification content for channel: {}", notification.channel());
        log.debug("Notification type: {}", notification.type());

        ResetPasswordContentPayload payload = (ResetPasswordContentPayload) notification.data();
        log.debug("Payload: {}", payload);

        Context context = new Context();
        context.setVariable(ContextVariablesHolder.SUBJECT, notification.bodyTitle());
        context.setVariable(ContextVariablesHolder.TEMPLATE, FragmentsReferencesHolder.RESET_PASSWORD);
        context.setVariable(ResetPasswordVariablesHolder.NEW_PASSWORD_BODY_VARIABLE_NAME, payload.newPassword());
        log.debug("Notification content was built successfully");
        return templateEngine.process(FragmentsReferencesHolder.BASE_NOTIFICATION, context);
    }
}
