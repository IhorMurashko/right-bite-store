package com.best_store.right_bite.security.jwtProvider;

import com.best_store.right_bite.security.constant.HeaderConstants;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.util.DateConstructorUtil;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * Default implementation of {@link JwtProvider} based on JJWT library.
 *
 * <p>
 * Uses HMAC SHA-256 signing with a shared secret key. Supports token generation with
 * subject, claims, type and configurable expiration.
 * </p>
 *
 * <p>
 * Token validation includes signature verification and expiration check.
 * </p>
 *
 * <p>
 * Author: Ihor Murashko
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProviderImpl implements JwtProvider {

    private final SecretKey key;

    @Override
    public String generateToken(@NonNull String subject,
                                @NonNull Long validityPeriodInSeconds,
                                @NonNull Map<String, Object> claims,
                                @NonNull TokenType tokenType) {

        Date now = new Date();
        Date expiryDate = DateConstructorUtil.dateExpirationGenerator(now, validityPeriodInSeconds);
        log.debug("expiryDate: {}", expiryDate);

        log.info("Generating JWT Token for subject '{}'", subject);
        return
                Jwts.builder()
                        .subject(subject)
                        .issuedAt(now)
                        .expiration(expiryDate)
                        .claims(claims)
                        .claim(TokenClaimsConstants.TOKEN_TYPE_CLAIM, tokenType)
                        .signWith(key)
                        .compact();
    }

    @Override
    public boolean validateToken(@NonNull String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            log.warn("Invalid token: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     * <p>
     * The method expects the header to begin with the string "Bearer ".
     * If the header is absent or does not follow this format, a warning is logged and {@code null} is returned.
     *
     * @param request the HttpServletRequest from which the token is to be extracted
     * @return the JWT token string if present and well-formed; {@code null} otherwise
     */
    @Override
    public String extractTokenFromHeader(@NonNull HttpServletRequest request) {
        final String requestToken = request.getHeader(HeaderConstants.HEADER_AUTHENTICATION);

        if (requestToken == null) {
            log.warn("There is no Authorization header");
            return null;
        }
        if (!requestToken.startsWith(HeaderConstants.BEARER_PREFIX)) {
            log.warn("Token does not begin with Bearer");
            return null;
        }
        return requestToken.substring(7);
    }
}
