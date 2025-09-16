package com.best_store.right_bite.notification.sender.email;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.holder.letter.DefaultSubjectHolder;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.notification.sender.NotificationSender;
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
    public void send(@NonNull BaseNotification<? extends ContentPayload> notification, @NonNull String content) {

        MimeMessage message = sender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            if (notification.bodyTitle() != null) {
                helper.setSubject(Objects.requireNonNull(notification.bodyTitle()));
            } else {
                helper.setSubject(DefaultSubjectHolder.DEFAULT_NOTIFICATION_SUBJECT);
            }
            log.debug("Notification content inside sender: {}", content);
            helper.setText(content, true);
            for (String next : notification.recipients()) {
                helper.setTo(next);
                log.info("Sending email to: {}", next);
                sender.send(message);
            }
        } catch (MessagingException ex) {
            log.error(ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
}