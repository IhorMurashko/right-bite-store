package com.best_store.right_bite.security.blackListTokenCache;

import org.springframework.lang.NonNull;

/**
 * Service interface for managing token revocation.
 *
 * <p>
 * Provides methods to revoke tokens and check if a token has been revoked.
 * Used to enforce session-level logout or force token invalidation.
 * </p>
 */
public interface RevokeTokenService {
    /**
     * Marks the given token(s) as revoked.
     *
     * @param token one or more token strings to revoke
     */
    void revokeToken(@NonNull String... token);

    /**
     * Checks whether the given token has been previously revoked.
     *
     * @param token token to check
     * @return true if revoked, false otherwise
     */
    boolean isTokenRevoked(@NonNull String token);
}