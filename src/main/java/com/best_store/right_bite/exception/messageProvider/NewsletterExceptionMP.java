package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * Provides predefined error messages related to newsletter subscription operations.
 *
 * <p>This utility class centralizes common newsletter subscription-related error messages,
 * ensuring a consistent and maintainable error messaging experience across the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * if (!exists) {
 *     throw new NewsletterSubscriptionsWasNotFound(
 *         String.format(NewsletterExceptionMP.NEWSLETTER_ID_WAS_NOT_FOUND, subscriptionId)
 *     );
 * }
 * }</pre>
 *
 * <p>Designed as a final class and not meant to be instantiated.</p>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class NewsletterExceptionMP {
    public final String NEWSLETTER_ID_WAS_NOT_FOUND = "Newsletter subscription with id %d not found";
    public final String NEWSLETTER_EMAIL_WAS_NOT_FOUND = "Newsletter subscription with email %s not found";
}