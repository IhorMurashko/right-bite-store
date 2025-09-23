package com.best_store.right_bite.security.managment;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Interface for managing token generation and related operations.
 * <p>
 * Provides methods to generate tokens, both special and default, as well
 * as facilitate the generation of standard claims required for token payloads.
 */
public interface TokenManager {

    /**
     * Generates a special token with specific claims, type, and validity period.
     *
     * @param subject                the unique identifier for the token subject (e.g., user ID or email).
     * @param claims                 a map of custom claims to be included in the token payload.
     * @param tokenType              the type of the token ({@code ACCESS} or {@code REFRESH}).
     * @param lifePeriodTokenInSecond the lifespan of the token in seconds.
     * @return a signed token string based on the provided parameters.
     */
    String generateSpecialToken(@NotNull String subject, @NotNull Map<String, Object> claims,
                                @NotNull TokenType tokenType, @NotNull Long lifePeriodTokenInSecond);

    /**
     * Generates default JWT tokens (access and refresh) for a given user.
     *
     * <p>The tokens include claims derived from the user's profile information
     * provided in the {@code DefaultUserInfoResponseDto}. The access token is used
     * for authentication, while the refresh token allows renewing the access token.</p>
     *
     * @param defaultUserInfoResponseDto the user information used to generate
     *                                    default claims; must not be null.
     * @return a {@link TokenDto} containing the access token and refresh token.
     */
    TokenDto generateDefaultTokens(@NotNull DefaultUserInfoResponseDto defaultUserInfoResponseDto);

    /**
     * Generates default claims for a token payload based on user information.
     * <p>
     * Claims include username (email) and roles derived from the provided
     * {@link DefaultUserInfoResponseDto}.
     *
     * @param defaultUserInfoResponseDto the user information DTO containing data
     *                                   for constructing the token claims. Must not be null.
     * @return a map of claims with keys defined in {@link TokenClaimsConstants}
     *         and corresponding values extracted from the user info.
     */
    Map<String, Object> generateDefaultClaims(@NotNull DefaultUserInfoResponseDto defaultUserInfoResponseDto);
}