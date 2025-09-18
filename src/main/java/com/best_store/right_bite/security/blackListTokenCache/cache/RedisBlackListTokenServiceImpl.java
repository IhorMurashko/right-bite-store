package com.best_store.right_bite.security.blackListTokenCache.cache;

import com.best_store.right_bite.security.claims.ClaimsProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

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
    /**
     * Saves revoked tokens into Redis with TTL until token expiration.
     *
     * @param tokens tokens to revoke
     */
    @Override
    public void saveToken(@NonNull String... tokens) {
        for (String t : tokens) {
            try {
                long accessTokenExpiration = claimsProvider.extractClaimFromToken(t, claims ->
                        claims.getExpiration().getTime());

                long accessTokenTTL = accessTokenExpiration - System.currentTimeMillis();

                if (accessTokenTTL > 0) {
                    redisTemplate.opsForValue().set(t, "revoked token", accessTokenTTL, TimeUnit.MILLISECONDS);
                    log.info("Revoked token {}, with TTL: {} ms", t, accessTokenTTL);
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
}
