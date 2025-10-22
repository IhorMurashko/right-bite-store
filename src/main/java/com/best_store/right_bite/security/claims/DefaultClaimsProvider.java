package com.best_store.right_bite.security.claims;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.function.Function;

/**
 * Default implementation of {@link ClaimsProvider} using JJWT.
 *
 * <p>
 * Parses a JWT token using a shared secret key and applies the provided function
 * to extract specific claims from the token's payload.
 * </p>
 *
 * <p>
 * Logs detailed error information if token is malformed.
 * </p>
 *
 * <p>
 * Author: Ihor Murashko
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultClaimsProvider implements ClaimsProvider {


    @Override
    public <T> T extractClaimFromToken(String token, SecretKey key, Function<Claims, T> function) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key).build()
                    .parseSignedClaims(token)
                    .getPayload();
            return function.apply(claims);
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token {}", ex.getMessage());
            throw ex;
        }
    }
}
