package com.best_store.right_bite.security.managment;


import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.utils.auth.TokensPropertiesDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Default implementation of {@link TokenManager} for managing JWT generation.
 * <p>
 * Relies on {@link JwtProvider} for actual token encoding and signing,
 * and retrieves token durations from {@link TokensPropertiesDispatcher}.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenManagerImpl implements TokenManager {

    private final TokensPropertiesDispatcher tokensPropertiesDispatcher;
    private final JwtProvider jwtProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public String generateSpecialToken(@NonNull String subject, @NonNull Map<String, Object> claims,
                                       @NonNull TokenType tokenType, @NonNull Long lifePeriodTokenInSecond) {
        return jwtProvider.generateToken(subject, lifePeriodTokenInSecond, claims, tokenType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenDto generateDefaultTokens(@NonNull DefaultUserInfoResponseDto defaultUserInfoResponseDto) {

        Map<String, Object> claims = generateDefaultClaims(defaultUserInfoResponseDto);

        String accessToken = jwtProvider.generateToken(String.valueOf(defaultUserInfoResponseDto.id()),
                tokensPropertiesDispatcher.getAccessTokenAvailableValidityPeriodInSec(),
                claims,
                TokenType.ACCESS
        );
        String refreshToken = jwtProvider.generateToken(String.valueOf(defaultUserInfoResponseDto),
                tokensPropertiesDispatcher.getRefreshTokenAvailableValidityPeriodInSec(),
                Map.of(TokenClaimsConstants.USERNAME_CLAIM, defaultUserInfoResponseDto.email()),
                TokenType.REFRESH
        );
        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> generateDefaultClaims(@NonNull DefaultUserInfoResponseDto defaultUserInfoDto) {
        return Map.of(
                TokenClaimsConstants.USERNAME_CLAIM, defaultUserInfoDto.email(),
                TokenClaimsConstants.ROLES_CLAIM, defaultUserInfoDto.roles());
    }

}