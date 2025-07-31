package com.best_store.right_bite.security.blackListTokenCache.cache;

import org.springframework.lang.NonNull;

/**
 * Abstraction for managing revoked JWT tokens.
 *
 * <p>Defines methods for saving revoked tokens and checking
 * whether a token is revoked.</p>
 *
 * <p>Implementations should ensure tokens expire from the store
 * after their TTL to prevent indefinite growth.</p>
 */
public interface BlackListTokenService {
    /**
     * Save one or more tokens to the revoked tokens store.
     *
     * @param tokens the tokens to mark as revoked
     */
    void saveToken(@NonNull String... tokens);

    /**
     * Checks if the given token is present in the revoked tokens store.
     *
     * @param token the token to check
     * @return true if the token is revoked, false otherwise
     */
    boolean isTokenPresent(@NonNull String token);
}
