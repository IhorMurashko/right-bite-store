package com.best_store.right_bite.security.blackListTokenCache;

import com.best_store.right_bite.security.blackListTokenCache.cache.BlackListTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Default implementation of {@link RevokeTokenService} that delegates token storage and lookup
 * to a {@link BlackListTokenService} implementation.
 *
 * <p>
 * This layer abstracts token revocation mechanics and enables switching
 * storage backends (e.g., Redis, in-memory, database).
 * </p>
 *
 * <p>
 * Author: Ihor Murashko
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DelegationRevokeTokenService implements RevokeTokenService {

    private final BlackListTokenService blackListTokenService;

    @Override
    public void revokeToken(@NonNull String... token) {
        blackListTokenService.saveToken(token);
    }

    @Override
    public boolean isTokenRevoked(@NonNull String token) {
        return blackListTokenService.isTokenPresent(token);
    }
}
