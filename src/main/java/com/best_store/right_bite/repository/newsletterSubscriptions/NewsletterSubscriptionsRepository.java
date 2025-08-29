package com.best_store.right_bite.repository.newsletterSubscriptions;

import com.best_store.right_bite.model.newsletterSubscription.NewsletterSubscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsletterSubscriptionsRepository extends JpaRepository<NewsletterSubscription, Long> {

    Optional<NewsletterSubscription> findByEmail(String email);

    void deleteByEmail(String email);

    boolean existsByEmail(String email);
}
