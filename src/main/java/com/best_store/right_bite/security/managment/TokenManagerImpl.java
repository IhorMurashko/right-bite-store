package com.best_store.right_bite.security.managment;


import com.best_store.right_bite.model.user.AbstractUser;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.util.TokensPropertiesDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenManagerImpl implements TokenManager {

    private final TokensPropertiesDispatcher tokensPropertiesDispatcher;
    private final JwtProvider jwtProvider;

    @Override
    public String generateToken(@NonNull String subject, @NonNull Map<String, Object> claims,
                                @NonNull TokenType tokenType, @NonNull Long lifePeriodTokenInSecond) {
        return jwtProvider.generateToken(subject, lifePeriodTokenInSecond, claims, tokenType);
    }

    @Override
    public TokenDto generateDefaultClaimsToken(@NonNull AbstractUser user) {
        Map<String, Object> claims = Map.of(
                TokenClaimsConstants.USERNAME_CLAIM, user.getEmail(),
                //todo: utils role parser
                TokenClaimsConstants.ROLES_CLAIM, user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList())
        );
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
}