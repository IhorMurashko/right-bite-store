package com.best_store.right_bite.security.managment;

import com.best_store.right_bite.model.user.AbstractUser;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import org.springframework.lang.NonNull;

import java.util.Map;
/**
 * Provides operations for generating JWT tokens and user-specific claims.
 */
public interface TokenManager {

    /**
     * Generates a JWT token with the specified subject, claims, token type, and expiration.
     *
     * @param subject the token subject (typically user ID)
     * @param claims custom claims to include in the token
     * @param tokenType type of the token (e.g., ACCESS, REFRESH)
     * @param lifePeriodTokenInSecond token expiration time in seconds
     * @return a signed JWT token as a String
     */
    String generateToken(@NonNull String subject, @NonNull Map<String, Object> claims,
                         @NonNull TokenType tokenType, @NonNull Long lifePeriodTokenInSecond);

    /**
     * Generates a pair of access and refresh tokens with default user claims.
     *
     * @param user the authenticated user
     * @return a DTO containing access and refresh tokens
     */
    TokenDto generateDefaultClaimsToken(@NonNull AbstractUser user);

    /**
     * Constructs default claims to be used in access tokens.
     * Includes roles and username (email).
     *
     * @param user the authenticated user
     * @return a map of JWT claims
     */
    Map<String, Object> generateDefaultUserClaims(@NonNull AbstractUser user);
}
