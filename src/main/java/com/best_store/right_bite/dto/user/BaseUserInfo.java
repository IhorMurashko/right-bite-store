package com.best_store.right_bite.dto.user;

/**
 * Base interface representing minimal user identity information.
 *
 * <p>Defines essential user identification fields used across the system.</p>
 *
 * <p>Implemented by various DTOs to standardize access to user ID and email.</p>
 *
 * @author Ihor Murashko
 */
public interface BaseUserInfo {
    /**
     * Returns the unique identifier of the user.
     *
     * @return user ID
     */
    long id();

    /**
     * Returns the email address of the user.
     *
     * @return user email
     */
    String email();
}

