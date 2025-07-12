package com.best_store.right_bite.util.notification.factory;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.notification.NotificationBuilderWasNotFoundException;
import com.best_store.right_bite.util.notification.contentBuilder.NotificationContentBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;


/**
 * Implementation of the {@link NotificationLetterBuilderFactory} interface.
 *
 * <p>Provides functionality to retrieve the appropriate {@link NotificationContentBuilder}
 * implementation based on a given {@link NotificationType} and {@link NotificationChannel}.
 * This is achieved by filtering beans from the application context that match the specified
 * type and channel.</p>
 *
 * <p>Throws {@link NotificationBuilderWasNotFoundException} if no matching builder is found
 * for the provided type and channel.</p>
 *
 * @author Ihor Murashko
 */
@Component
@RequiredArgsConstructor
@Service
public class NotificationLetterBuilderFactoryImpl implements NotificationLetterBuilderFactory {

    private final ApplicationContext context;

    @Override
    public NotificationContentBuilder getNotificationBuilder(NotificationType type, NotificationChannel channel) {

        Supplier<NotificationContentBuilder> builder = () ->
                context.getBeansOfType(NotificationContentBuilder.class).values()
                        .stream()
                        .filter(bean -> bean.getNotificationType().equals(type))
                        .filter(bean -> bean.getNotificationChannel().equals(channel))
                        .findFirst().
                        orElseThrow(() ->
                                new NotificationBuilderWasNotFoundException(
                                        String.format(
                                                ExceptionMessageProvider.NOTIFICATION_BUILDER_WAS_NOT_FOUND, type, channel
                                        )
                                ));
        return builder.get();
    }
}
