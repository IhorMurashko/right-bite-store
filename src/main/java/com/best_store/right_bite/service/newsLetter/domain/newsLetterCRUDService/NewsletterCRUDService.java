package com.best_store.right_bite.service.newsLetter.domain.newsLetterCRUDService;

import com.best_store.right_bite.model.newsletterSubscription.NewsletterSubscription;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface NewsletterCRUDService {
    NewsletterSubscription save(@NotNull NewsletterSubscription newsletterSubscription);

    Optional<NewsletterSubscription> findById(@NotNull Long id);

    Optional<NewsletterSubscription> findByEmail(@NotNull String email);

    void deleteById(@NotNull Long id);

    void deleteByEmail(@NotNull String email);

    boolean existsByEmail(@NotNull String email);
}
