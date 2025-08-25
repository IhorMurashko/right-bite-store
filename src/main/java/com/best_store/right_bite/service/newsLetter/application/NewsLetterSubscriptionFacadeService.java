package com.best_store.right_bite.service.newsLetter.application;

import com.best_store.right_bite.dto.notification.request.NotificationSubscriptionRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface NewsLetterSubscriptionFacadeService {
    void subscribe(@NotNull @Valid NotificationSubscriptionRequestDto notificationSubscriptionRequestDto);

    void unsubscribe(@NotNull @Valid NotificationSubscriptionRequestDto notificationSubscriptionRequestDto);
}
