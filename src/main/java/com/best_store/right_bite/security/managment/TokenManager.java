package com.best_store.right_bite.security.managment;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
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
     * @param subject                 the token subject (typically user ID)
     * @param claims                  custom claims to include it in the token
     * @param tokenType               type of the token (e.g., ACCESS, REFRESH)
     * @param lifePeriodTokenInSecond token expiration time in seconds
     * @return a signed JWT token as a String
     */
    String generateSpecialToken(@NonNull String subject, @NonNull Map<String, Object> claims,
                                @NonNull TokenType tokenType, @NonNull Long lifePeriodTokenInSecond);

    TokenDto generateDefaultTokens(@NonNull DefaultUserInfoResponseDto defaultUserInfoResponseDto);

    Map<String, Object> generateDefaultClaims(@NonNull DefaultUserInfoResponseDto defaultUserInfoResponseDto);
}
