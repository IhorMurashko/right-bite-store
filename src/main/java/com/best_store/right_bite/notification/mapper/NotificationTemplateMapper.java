package com.best_store.right_bite.notification.mapper;

import com.best_store.right_bite.notification.data.NotificationData;
import org.thymeleaf.context.Context;

public interface NotificationTemplateMapper<T extends NotificationData> {
    void applyVariables(Context context, T data);
}
