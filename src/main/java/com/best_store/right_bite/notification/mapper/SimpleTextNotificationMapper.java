package com.best_store.right_bite.notification.mapper;

import com.best_store.right_bite.notification.data.SimpleTextNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
@Slf4j
public class SimpleTextNotificationMapper implements NotificationTemplateMapper<SimpleTextNotification> {

    @Override
    public void applyVariables(Context context, SimpleTextNotification data) {
        String content = data.content();
        context.setVariable("content", content);
        log.debug("Applied content data to the notification letter context");
    }
}
