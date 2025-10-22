package com.best_store.right_bite.security.jwtProvider;

import com.best_store.right_bite.security.constant.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKey;
import java.util.Map;

/**
 * Defines the contract for JWT token generation and validation.
 *
 * <p>
 * Implementations are responsible for producing signed JWT tokens and verifying
 * their validity according to the security configuration of the application.
 * </p>
 */
public interface JwtProvider {
    /**
     * Generates a signed JWT token.
     *
     * @param subject                 the principal identity (e.g., username or user ID)
     * @param validityPeriodInSeconds token validity in seconds
     * @param claims                  custom claims to include in the payload
     * @param type                    type of the token (ACCESS or REFRESH)
     * @return encoded JWT token string
     */
    String generateToken(String subject, Long validityPeriodInSeconds,
                         Map<String, Object> claims, TokenType type, SecretKey key);

    /**
     * Validates a JWT token and checks its signature and expiration.
     *
     * @param token the raw JWT string
     * @return true if the token is valid; false otherwise
     */
    boolean validateToken(String token, SecretKey key);

    /**
     * Extracts the JWT token from the Authorization header of the given HTTP request.
     * <p>
     * The method expects the Authorization header to begin with the string "Bearer ".
     * If the header is absent or improperly formatted, it logs a warning and returns {@code null}.
     *
     * @param request the {@link HttpServletRequest} containing the Authorization header
     * @return the extracted JWT token as a {@link String}, or {@code null} if not present or improperly formatted
     */
    String extractTokenFromHeader(@NonNull HttpServletRequest request);
}