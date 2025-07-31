package com.best_store.right_bite.security.managment;


import com.best_store.right_bite.model.user.AbstractUser;
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
import java.util.stream.Collectors;

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
    public String generateToken(@NonNull String subject, @NonNull Map<String, Object> claims,
                                @NonNull TokenType tokenType, @NonNull Long lifePeriodTokenInSecond) {
        return jwtProvider.generateToken(subject, lifePeriodTokenInSecond, claims, tokenType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TokenDto generateDefaultClaimsToken(@NonNull AbstractUser user) {

        Map<String, Object> claims = generateDefaultUserClaims(user);

        String accessToken = jwtProvider.generateToken(String.valueOf(user.getId()),
                tokensPropertiesDispatcher.getAccessTokenAvailableValidityPeriodInSec(),
                claims,
                TokenType.ACCESS
        );
        String refreshToken = jwtProvider.generateToken(String.valueOf(user.getId()),
                tokensPropertiesDispatcher.getRefreshTokenAvailableValidityPeriodInSec(),
                Map.of(TokenClaimsConstants.USERNAME_CLAIM, user.getEmail()),
                TokenType.REFRESH
        );
        return new TokenDto(accessToken, refreshToken);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> generateDefaultUserClaims(@NonNull AbstractUser user) {
        return Map.of(
                TokenClaimsConstants.USERNAME_CLAIM, user.getEmail(),
                TokenClaimsConstants.ROLES_CLAIM, user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList())
        );
    }

}