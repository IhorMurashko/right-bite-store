package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NewsletterExceptionMP {
    public final String NEWSLETTER_ID_WAS_NOT_FOUND = "Newsletter subscription with id %d not found";
    public final String NEWSLETTER_EMAIL_WAS_NOT_FOUND = "Newsletter subscription with email %s not found";
}
