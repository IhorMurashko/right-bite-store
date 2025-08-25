package com.best_store.right_bite.service.newsLetter.domain.newsLetterCRUDService;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.notificationSubscription.NewsletterSubscriptionsWasNotFound;
import com.best_store.right_bite.model.newsletterSubscription.NewsletterSubscription;
import com.best_store.right_bite.repository.newsletterSubscriptions.NewsletterSubscriptionsRepository;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewsletterCRUDServiceImpl implements NewsletterCRUDService {

    private final NewsletterSubscriptionsRepository newsletterSubscriptionsRepository;

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public NewsletterSubscription save(@NotNull NewsletterSubscription newsletterSubscription) {
        log.info("Saving newsletter subscription: {} with status {}", newsletterSubscription, newsletterSubscription.isSubscribed());
        return newsletterSubscriptionsRepository.save(newsletterSubscription);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public Optional<NewsletterSubscription> findById(@NotNull Long id) {
        log.debug("Finding newsletter subscription by id: {}", id);
        return newsletterSubscriptionsRepository.findById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public Optional<NewsletterSubscription> findByEmail(@NotNull String email) {
        log.debug("Finding newsletter subscription by email: {}", email);
        return newsletterSubscriptionsRepository.findByEmail(UserFieldAdapter.toLower(email));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(@NotNull Long id) {
        log.warn("Deleting newsletter subscription by id: {}", id);
        if (newsletterSubscriptionsRepository.existsById(id)) {
            newsletterSubscriptionsRepository.deleteById(id);
        } else {
            throw new NewsletterSubscriptionsWasNotFound(String.format(
                    ExceptionMessageProvider.NEWSLETTER_ID_WAS_NOT_FOUND, id
            ));
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void deleteByEmail(@NotNull String email) {
        String emailLower = UserFieldAdapter.toLower(email);
        log.warn("Deleting newsletter subscription by email: {}", emailLower);
        if (newsletterSubscriptionsRepository.existsByEmail(UserFieldAdapter.toLower(emailLower))) {
            newsletterSubscriptionsRepository.deleteByEmail(emailLower);
        } else {
            throw new NewsletterSubscriptionsWasNotFound(String.format(
                    ExceptionMessageProvider.NEWSLETTER_EMAIL_WAS_NOT_FOUND, emailLower
            ));
        }
    }

    @Override
    public boolean existsByEmail(@NotNull String email) {
        return newsletterSubscriptionsRepository.existsByEmail(UserFieldAdapter.toLower(email));
    }
}
