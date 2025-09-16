package com.best_store.right_bite.utils.notification.cache.senders;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.notification.SenderWasNotFoundException;
import com.best_store.right_bite.notification.sender.NotificationSender;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class SenderFactoryImpl implements SenderFactory {

    private final ApplicationContext context;
    private Map<NotificationChannel, NotificationSender> sendersCache;

    @Override
    public NotificationSender getNotificationSender(@NonNull NotificationChannel channel) {
        NotificationSender sender = sendersCache.get(channel);
        if (sender == null) {
            throw new SenderWasNotFoundException(
                    ExceptionMessageProvider.SENDER_BY_CHANNEL_WAS_NOT_FOUND
            );
        }
        return sender;
    }


    @PostConstruct
    private void fillSendersCache() {
        this.sendersCache = new ConcurrentHashMap<>();
        context.getBeansOfType(NotificationSender.class)
                .values()
                .stream()
                .filter(Objects::nonNull)
                .forEach(sender ->
                        this.sendersCache.computeIfAbsent(
                                sender.getNotificationChannel(), key -> sender));
    }
}
