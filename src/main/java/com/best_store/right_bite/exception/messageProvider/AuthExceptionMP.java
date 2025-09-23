package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * Provides predefined error messages related to authentication processes.
 *
 * <p>This utility class centralizes common authentication-related error messages,
 * ensuring consistency across the application.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new CredentialsException(AuthExceptionMP.USER_ACCOUNT_IS_EXPIRED);
 * }</pre>
 *
 * <p>Designed as a final class and not meant to be instantiated.</p>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class AuthExceptionMP {
    public final String USER_ACCOUNT_IS_EXPIRED = "User account is expired.";
    public final String PASSWORDS_DONT_MATCH = "Passwords don't match";
    public final String EMAIL_ALREADY_EXIST = "Email: %s already exist";
}
