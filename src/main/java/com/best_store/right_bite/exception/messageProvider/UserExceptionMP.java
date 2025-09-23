package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * Provides predefined error messages related to user operations.
 *
 * <p>This utility class centralizes common user-related error messages,
 * ensuring consistency and maintainability across the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new UserNotFoundException(
 *     String.format(UserExceptionMP.USER_EMAIL_NOT_FOUND, userEmail)
 * );
 * }</pre>
 *
 * <p>Designed as a final utility class to prevent instantiation and inheritance.</p>
 *
 * <p>Key error messages:</p>
 * <ul>
 *   <li>{@code USER_EMAIL_NOT_FOUND} - Indicates that no user was found for a given email.</li>
 *   <li>{@code USER_ID_NOT_FOUND} - Indicates that no user was found for a given ID.</li>
 * </ul>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class UserExceptionMP {
    public final String USER_EMAIL_NOT_FOUND = "User with email: %s not found";
    public final String USER_ID_NOT_FOUND = "User with ID: %d not found";
}