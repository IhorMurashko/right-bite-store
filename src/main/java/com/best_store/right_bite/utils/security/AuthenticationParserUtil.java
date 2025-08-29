package com.best_store.right_bite.utils.security;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.role.InvalidPrincipalCastException;
import com.best_store.right_bite.security.exception.InvalidTokenSubjectException;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Utility component for extracting user-related information from Spring Security's {@link Authentication} object.
 * <p>
 * Currently, this utility supports extracting the authenticated user's ID from a {@link JwtPrincipal}.
 * Throws specific exceptions if the principal is of an unexpected type or if the ID cannot be parsed.
 * </p>
 * <p>
 * This class is intended to be used in service or facade layers where the authenticated user's ID
 * is required for business logic or caching purposes.
 * </p>
 */
@Component
@Slf4j
public class AuthenticationParserUtil {
    /**
     * Extracts the authenticated user's ID from the provided authentication object.
     * Only supports {@link JwtPrincipal}.
     *
     * @param authentication current security context
     * @return user ID
     * @throws InvalidTokenSubjectException  if ID cannot be parsed
     * @throws InvalidPrincipalCastException if the principal is not a {@link JwtPrincipal}
     */
    public Long getUserLongIdFromAuthentication(@NonNull Authentication authentication) {
        if (authentication.getPrincipal() instanceof JwtPrincipal principal) {
            try {
                return Long.parseLong(principal.id());
            } catch (NumberFormatException ex) {
                log.warn("Authentication subject principal is not a long: {}", principal.id().getClass().getSimpleName());
                throw new InvalidTokenSubjectException(
                        String.format(
                                ExceptionMessageProvider.INVALID_TOKEN_SUBJECT,
                                ex.getClass().getSimpleName()));
            }
        } else {
            throw new InvalidPrincipalCastException(
                    ExceptionMessageProvider.AUTHENTICATION_CAST_INSTANCE_CAST_EXCEPTION);
        }
    }
}
