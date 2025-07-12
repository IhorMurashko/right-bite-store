package com.best_store.right_bite.service.auth.update.token;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.auth.InvalidTokenException;
import com.best_store.right_bite.exception.auth.RefreshTokenAccessException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.security.managment.TokenManager;
import com.best_store.right_bite.service.user.UserCrudService;
import com.best_store.right_bite.util.auth.TokensPropertiesDispatcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final TokenManager tokenManager;
    private final JwtProvider jwtProvider;
    private final UserCrudService userCrudService;
    private final ClaimsProvider claimsProvider;
    private final SecretKey key;
    private final TokensPropertiesDispatcher tokensPropertiesDispatcher;

    @Override
    public TokenDto refreshToken(@NonNull @Valid TokenDto tokenDto) {
        String refreshToken = tokenDto.refreshToken();


        if (jwtProvider.validateToken(refreshToken)) {
            TokenType tokenType = claimsProvider.extractClaimFromToken(refreshToken, claims ->
                    claims.get(TokenClaimsConstants.TOKEN_TYPE_CLAIM, TokenType.class));
            log.debug("refresh token type: {}", tokenType);
            if (tokenType != TokenType.REFRESH) {
                log.error("Invalid refresh token for");
                //todo: exception handler
                throw new RefreshTokenAccessException(
                        String.format(
                                ExceptionMessageProvider.TOKEN_ACCESS_EXCEPTION, tokenType.name()
                        )
                );
            }

            String userId = claimsProvider.extractClaimFromToken(refreshToken, claims ->
                    claims.get(TokenClaimsConstants.USER_ID_CLAIM, String.class));
            log.debug("refresh token user id: {}", userId);

            User user = userCrudService.findById(Long.valueOf(userId));
            log.info("user with id {} was found", userId);

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            Date refreshTokenExpiration = claims.getExpiration();
            Instant expirationInstant = refreshTokenExpiration.toInstant();
            Instant nowInstant = Instant.now();
            long hoursUntilExpiration = ChronoUnit.HOURS.between(nowInstant, expirationInstant);

            if (hoursUntilExpiration < 23) {
                log.info("access and refresh tokens will be generated");
                return tokenManager.generateDefaultClaimsToken(user);
            } else {
                Map<String, Object> defaultClaims = tokenManager.generateDefaultUserClaims(user);
                String newAccessToken = tokenManager.generateToken(
                        String.valueOf(user.getId()),
                        defaultClaims,
                        TokenType.ACCESS,
                        tokensPropertiesDispatcher.getAccessTokenAvailableValidityPeriodInSec()
                );
                log.info("new access token was generated");
                return new TokenDto(refreshToken, newAccessToken);
            }
        } else {
            //todo: exception handler
            throw new InvalidTokenException(
                    String.format(
                            ExceptionMessageProvider.INVALID_TOKEN
                    )
            );
        }
    }
}
