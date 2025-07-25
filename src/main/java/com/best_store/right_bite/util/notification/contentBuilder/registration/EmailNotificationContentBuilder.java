package com.best_store.right_bite.util.notification.contentBuilder.registration;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.dto.notification.BaseNotification;
import com.best_store.right_bite.util.notification.contentBuilder.NotificationContentBuilder;
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
 *   <li>Notification Type: {@code NotificationType.REGISTRATION}</li>
 *   <li>Channel: {@code NotificationChannel.EMAIL}</li>
 * </ul>
 * </p>
 *
 * <p>Template Path:
 * <ul>
 *   <li>{@code "notification/email/registration"}</li>
 * </ul>
 * </p>
 *
 * @author Ihor Murashko
 */
@Component
@Slf4j
public class EmailNotificationContentBuilder implements NotificationContentBuilder {

    private final NotificationType notificationType;
    private final NotificationChannel notificationChannel;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    public EmailNotificationContentBuilder(SpringTemplateEngine templateEngine) {
        this.notificationType = NotificationType.REGISTRATION;
        this.notificationChannel = NotificationChannel.EMAIL;
        this.templateEngine = templateEngine;
    }

    //todo: make information notification builder
    @Override
    public String build(@NonNull BaseNotification notification) {
        Context context = new Context();
        return templateEngine.process("notification/email/registration", context);
    }

    @Override
    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    @Override
    public NotificationChannel getNotificationChannel() {
        return this.notificationChannel;
    }
}
