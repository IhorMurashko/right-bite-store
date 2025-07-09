package com.best_store.right_bite.security.blackListTokenCache.cache;

import org.springframework.lang.NonNull;

/**
 * Storage abstraction for revoked tokens.
 *
 * <p>
 * Defines how tokens are stored and checked for revocation (e.g., via Redis or local store).
 * Implementations must ensure TTL-based expiry to avoid permanent storage.
 * </p>
 */
public interface RedisBlackListTokenService {
    /**
     * Saves one or more tokens to the revoked store.
     *
     * @param tokens tokens to store as revoked
     */
    void saveToken(@NonNull String... tokens);

    /**
     * Checks whether a given token is in the revoked store.
     *
     * @param token token to verify
     * @return true if token is revoked
     */
    boolean isTokenPresent(@NonNull String token);
}
