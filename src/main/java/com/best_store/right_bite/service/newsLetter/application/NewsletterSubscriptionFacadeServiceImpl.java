package com.best_store.right_bite.service.newsLetter.application;

import com.best_store.right_bite.dto.notification.request.NotificationSubscriptionRequestDto;
import com.best_store.right_bite.exception.messageProvider.NewsletterExceptionMP;
import com.best_store.right_bite.exception.exceptions.notificationSubscription.NewsletterSubscriptionsWasNotFound;
import com.best_store.right_bite.model.newsletterSubscription.NewsletterSubscription;
import com.best_store.right_bite.service.newsLetter.domain.newsLetterCRUDService.NewsletterCRUDService;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
@Slf4j
public class NewsletterSubscriptionFacadeServiceImpl implements NewsLetterSubscriptionFacadeService {

    private final NewsletterCRUDService newsLetterCRUDService;

    @Override
    public void subscribe(@NotNull @Valid NotificationSubscriptionRequestDto notificationSubscriptionRequestDto) {
        String adaptedEmail = UserFieldAdapter.toLower(notificationSubscriptionRequestDto.email());
        boolean isEmailExisting = newsLetterCRUDService.existsByEmail(adaptedEmail);
        if (isEmailExisting) {
            newsLetterCRUDService.findByEmail(adaptedEmail)
                    .ifPresent(newsletterSubscription -> {
                        if (newsletterSubscription.isSubscribed()) {
                            log.info("Newsletter subscription for email: {} already subscribed", adaptedEmail);
                        } else {
                            newsletterSubscription.setSubscribed(true);
                            newsLetterCRUDService.save(newsletterSubscription);
                            log.info("Newsletter subscription for with email: {} was subscribed", adaptedEmail);
                        }
                    });
        } else {
            NewsletterSubscription newsletterSubscription = new NewsletterSubscription(adaptedEmail, true);
            newsLetterCRUDService.save(newsletterSubscription);
            log.info("Created new newsletter subscription for email: {}", adaptedEmail);
        }
    }

    @Override
    public void unsubscribe(@NotNull @Valid NotificationSubscriptionRequestDto notificationSubscriptionRequestDto) {
        String adaptedEmail = UserFieldAdapter.toLower(notificationSubscriptionRequestDto.email());
        NewsletterSubscription newsletterSubscription = newsLetterCRUDService.findByEmail(adaptedEmail)
                .orElseThrow(() -> new NewsletterSubscriptionsWasNotFound(
                        String.format(NewsletterExceptionMP.NEWSLETTER_EMAIL_WAS_NOT_FOUND, adaptedEmail)
                ));
        if (newsletterSubscription.isSubscribed()) {
            newsletterSubscription.setSubscribed(false);
            newsLetterCRUDService.save(newsletterSubscription);
            log.info("User with email: {} unsubscribed from newsletter", adaptedEmail);
        } else {
            log.warn("User with email: {} already unsubscribed from newsletter", adaptedEmail);
        }
    }
}
