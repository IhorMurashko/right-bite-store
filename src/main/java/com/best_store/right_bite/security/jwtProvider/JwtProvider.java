package com.best_store.right_bite.security.jwtProvider;

import com.best_store.right_bite.security.constant.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;

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
                         Map<String, Object> claims, TokenType type);

    /**
     * Validates a JWT token and checks its signature and expiration.
     *
     * @param token the raw JWT string
     * @return true if token is valid; false otherwise
     */
    boolean validateToken(String token);

     String extractTokenFromHeader(@NonNull HttpServletRequest request);

}
