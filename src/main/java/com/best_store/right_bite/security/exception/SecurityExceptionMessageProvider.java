package com.best_store.right_bite.security.exception;

import lombok.experimental.UtilityClass;

/**
 * Utility class providing predefined messages for security-related exceptions.
 * <p>
 * This class offers a centralized source of error messages to be reused across the application
 * for handling security exceptions such as revoked tokens, invalid token types, and unauthorized access.
 * The class ensures consistency and reduces duplication of message definitions.
 * </p>
 */
@UtilityClass
public class SecurityExceptionMessageProvider {
    public final String TOKEN_WAS_REVOKED
            = "Token was revoked";
    public final String INVALID_TOKEN_TYPE
            = "Invalid token type %s";
    public final String UNAUTHORIZED_EXCEPTION_MESSAGE
            = "Error: Unauthorized";
    public static final String TOKEN_RESPONSE_TEMPLATE = "{\"error\": \"%s\"}";
}