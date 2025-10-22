package com.best_store.right_bite.security.blackListTokenCache.cache;

import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.util.ApplicationSecretKeysHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.concurrent.TimeUnit;

/**
 * Redis-backed implementation of {@link BlackListTokenService}.
 *
 * <p>Stores revoked tokens in Redis with expiration matching the token's TTL.</p>
 *
 * <p>Uses {@link ClaimsProvider} to extract expiration date from tokens.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RedisBlackListTokenServiceImpl implements BlackListTokenService {

    private final StringRedisTemplate redisTemplate;
    private final ClaimsProvider claimsProvider;
    private final ApplicationSecretKeysHolder key;

    @Override
    public void saveToken(@NonNull TokenDto... tokenDtos) {
        for (TokenDto t : tokenDtos) {
            try {
                String accessToken = t.accessToken();
                String refreshToken = t.refreshToken();
                long accessTokenTTL = getTokenTTL(accessToken, key.getJwtAccessSecretKey());
                long refreshTokenTTL = getTokenTTL(refreshToken, key.getJwtRefreshSecretKey());

                if (accessTokenTTL > 0) {
                    redisTemplate.opsForValue().set(accessToken, "revoked token", accessTokenTTL, TimeUnit.MILLISECONDS);
                    log.info("Revoked token {}, with TTL: {} ms", accessToken, accessTokenTTL);
                }
                if (refreshTokenTTL > 0) {
                    redisTemplate.opsForValue().set(refreshToken, "revoked token", refreshTokenTTL, TimeUnit.MILLISECONDS);
                    log.info("Revoked token {}, with TTL: {} ms", refreshToken, refreshTokenTTL);
                }
            } catch (Exception e) {
                log.warn("Failed to revoke tokens: {}", t, e);
            }
        }
    }

    /**
     * Checks if a token is present (revoked) in Redis.
     *
     * @param token token to check
     * @return true if the token is revoked
     */
    @Override
    public boolean isTokenPresent(@NonNull String token) {
        return redisTemplate.hasKey(token);
    }

    private long getTokenTTL(String token, SecretKey key) {
        long tokenExpiration = claimsProvider.extractClaimFromToken(
                token, key, claims -> claims.getExpiration().getTime());
        return tokenExpiration - System.currentTimeMillis();
    }
}