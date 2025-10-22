package com.best_store.right_bite.security.blackListTokenCache;

import com.best_store.right_bite.security.dto.TokenDto;
import org.springframework.lang.NonNull;


public interface RevokeTokenService {

    void revokeToken(@NonNull TokenDto... tokenDtos);

    /**
     * Checks whether the given token has been previously revoked.
     *
     * @param token token to check
     * @return true if revoked, false otherwise
     */
    boolean isTokenRevoked(@NonNull String token);
}