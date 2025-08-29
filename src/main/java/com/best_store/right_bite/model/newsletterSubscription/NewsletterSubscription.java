package com.best_store.right_bite.model.newsletterSubscription;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "newsletter_subscriptions")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class NewsletterSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_letter_subscriptions_seq")
    @SequenceGenerator(name = "news_letter_subscriptions_seq", sequenceName = "news_letter_subscriptions_seq", allocationSize = 1)
    protected Long id;

    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private boolean isSubscribed;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public NewsletterSubscription(String email, boolean isSubscribed) {
        this.email = email;
        this.isSubscribed = isSubscribed;
        this.createdAt = LocalDateTime.now();
    }
}
