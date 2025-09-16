package com.best_store.right_bite.utils.notification.contentProcessor;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.notification.NotificationProcessingException;
import com.best_store.right_bite.notification.data.core.BaseNotification;
import com.best_store.right_bite.notification.data.payload.ContentPayload;
import com.best_store.right_bite.utils.notification.contentBuilder.NotificationContentBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationContentProcessorImpl implements NotificationContentProcessor {

    @SuppressWarnings("unchecked")
    @Override
    public String processContent(@NonNull NotificationContentBuilder<? extends ContentPayload> builder,
                                 @NonNull BaseNotification<? extends ContentPayload> notification) {
        try {
            NotificationContentBuilder<ContentPayload> genericBuilder =
                    (NotificationContentBuilder<ContentPayload>) builder;
            BaseNotification<ContentPayload> genericNotification =
                    (BaseNotification<ContentPayload>) notification;
            return genericBuilder.build(genericNotification);
        } catch (ClassCastException e) {
            log.error("Type mismatch between builder {} and notification {}",
                    builder.getClass().getSimpleName(),
                    notification.getClass().getSimpleName(), e);
            throw new NotificationProcessingException(
                    String.format(
                            ExceptionMessageProvider.NOTIFICATION_BUILDER_AND_TYPES_ARE_NOT_INCOMPATIBLE, e.getMessage()
                    ));
        } catch (Exception e) {
            log.error("Error processing notification content", e);
            throw new NotificationProcessingException(
                    String.format(
                            ExceptionMessageProvider.FAILED_TO_PROCESS_NOTIFICATION_CONTENT, e.getMessage()));
        }
    }
}
