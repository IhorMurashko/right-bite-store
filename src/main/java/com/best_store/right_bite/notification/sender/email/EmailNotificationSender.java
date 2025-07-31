package com.best_store.right_bite.notification.sender.email;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.email.EmailLetterContent;
import com.best_store.right_bite.notification.NotificationSender;
import com.best_store.right_bite.notification.data.BaseNotification;
import com.best_store.right_bite.notification.data.NotificationData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Implementation of the {@link NotificationSender} interface
 * responsible for sending email notifications using the
 * {@link JavaMailSender} service.
 * <p>
 * This class ensures that notifications are sent via the
 * {@link NotificationChannel#EMAIL} channel. It constructs and sends
 * email messages with support for optional subjects and HTML content.
 * If any errors occur during email dispatch, they will be logged and
 * runtime exceptions will be thrown.
 * <p>
 * The sending process is asynchronous to minimize blocking.
 * <p>
 * Dependencies:
 * - {@link JavaMailSender}: Used for constructing and sending email messages.
 * <p>
 * Annotations:
 * - {@code @Component}: To mark this class as a Spring-managed bean.
 * - {@code @Slf4j}: For logging errors and other information.
 * - {@code @Async}: To perform email sending in a separate thread.
 * <p>
 * Thread Safety:
 * This class is thread-safe as it primarily uses Spring's thread-safe beans.
 *
 * @author Ihor Murashko
 */
@Component
@Slf4j
@Getter
public class EmailNotificationSender implements NotificationSender {

    private final NotificationChannel notificationChannel;
    private final JavaMailSender sender;

    @Autowired
    public EmailNotificationSender(JavaMailSender sender) {
        this.notificationChannel = NotificationChannel.EMAIL;
        this.sender = sender;
    }

    @Async
    @Override
    public void send(@NonNull BaseNotification<? extends NotificationData> notification, @NonNull String content) {

        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(notification.to());

            if (notification.subject() != null) {
                helper.setSubject(Objects.requireNonNull(notification.subject()));
            } else {
                helper.setSubject(EmailLetterContent.DEFAULT_NOTIFICATION_SUBJECT);
            }
            helper.setText(content, true);
            sender.send(message);
        } catch (MessagingException ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
}
