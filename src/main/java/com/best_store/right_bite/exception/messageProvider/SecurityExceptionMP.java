package com.best_store.right_bite.exception.messageProvider;

import lombok.experimental.UtilityClass;

/**
 * Provides predefined error messages related to security exceptions.
 *
 * <p>This utility class centralizes common security-related error messages,
 * ensuring consistent and maintainable error message handling across the
 * application.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * throw new SecurityException(String.format(SecurityExceptionMP.TOKEN_ACCESS_EXCEPTION, tokenType));
 * }</pre>
 *
 * <p>Designed as a final utility class to prevent instantiation and inheritance.</p>
 *
 * <p>Key error messages:</p>
 * <ul>
 *   <li>{@code TOKEN_ACCESS_EXCEPTION} - Indicates that a specific token type cannot be used for accessing an endpoint.</li>
 *   <li>{@code INVALID_TOKEN} - Indicates an invalid token is provided.</li>
 *   <li>{@code AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION} - Indicates an invalid cast of authentication principal.</li>
 *   <li>{@code INVALID_TOKEN_SUBJECT} - Indicates an invalid token subject, specifying what subject type is unacceptable.</li>
 * </ul>
 *
 * @author Ihor Murashko
 */
@UtilityClass
public class SecurityExceptionMP {
    public final String TOKEN_ACCESS_EXCEPTION = "Token with type %s can't be used for access for this endpoint.";
    public final String INVALID_TOKEN = "Invalid token.";
    public final String AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION = "Authentication principal is not instance of JwtPrincipal.";
    public final String INVALID_TOKEN_SUBJECT = "Invalid token subject. Subject couldn't be %s type.";
}