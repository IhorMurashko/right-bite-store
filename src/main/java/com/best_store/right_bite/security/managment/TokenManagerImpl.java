package com.best_store.right_bite.security.managment;


import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenManagerImpl implements TokenManager {

    @Value("${accessTokenAvailableValidityPeriodInSec}")
    private Long accessTokenAvailableValidityPeriodInSec;
    @Value("${refreshTokenAvailableValidityPeriodInSec}")
    private Long refreshTokenAvailableValidityPeriodInSec;

    private final JwtProvider jwtProvider;

    public TokenDto generateToken(@NonNull String userId, @Nullable Map<String, Object> claims) {

        String accessToken = jwtProvider.generateToken(
                userId,
                accessTokenAvailableValidityPeriodInSec,
                claims,
                TokenType.ACCESS);
        log.debug("accessToken was created");

        String refreshTokenToken = jwtProvider.generateToken(
                userId,
                refreshTokenAvailableValidityPeriodInSec,
                claims,
                TokenType.REFRESH);
        log.debug("refreshToken was created");

        return new TokenDto(accessToken, refreshTokenToken);
    }
}