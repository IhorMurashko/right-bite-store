package com.best_store.right_bite.service.auth.update.token;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.exception.exceptions.auth.InvalidTokenException;
import com.best_store.right_bite.exception.exceptions.auth.RefreshTokenAccessException;
import com.best_store.right_bite.exception.messageProvider.SecurityExceptionMP;
import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.security.managment.TokenManager;
import com.best_store.right_bite.security.util.ApplicationSecretKeysHolder;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.auth.TokensPropertiesDispatcher;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final TokenManager tokenManager;
    private final JwtProvider jwtProvider;
    private final UserCrudService userCrudService;
    private final ClaimsProvider claimsProvider;
    private final ApplicationSecretKeysHolder applicationSecretKeysHolder;
    private final TokensPropertiesDispatcher tokensPropertiesDispatcher;

    @Override
    public TokenDto refreshToken(@NonNull @Valid TokenDto tokenDto) {
        String refreshToken = tokenDto.refreshToken();
        if (jwtProvider.validateToken(refreshToken, applicationSecretKeysHolder.getJwtRefreshSecretKey())) {
            String tokenType = claimsProvider.extractClaimFromToken(refreshToken, applicationSecretKeysHolder.getJwtRefreshSecretKey(), claims ->
                    claims.get(TokenClaimsConstants.TOKEN_TYPE_CLAIM, String.class));
            log.debug("refresh token type: {}", tokenType);
            if (!Objects.equals(tokenType, TokenType.REFRESH.name())) {
                log.error("Invalid refresh token for");
                throw new RefreshTokenAccessException(
                        String.format(
                                SecurityExceptionMP.TOKEN_ACCESS_EXCEPTION, tokenType
                        )
                );
            }
            Claims claims = Jwts.parser()
                    .verifyWith(applicationSecretKeysHolder.getJwtRefreshSecretKey())
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

            String userId = claims.getSubject();
            Long id;
            try {
                id = Long.parseLong(userId);
            } catch (NullPointerException ex) {
                log.error("Refresh token subject is null");
                throw new InvalidTokenException(String.format(
                        SecurityExceptionMP.INVALID_TOKEN_SUBJECT, "null"));
            } catch (NumberFormatException ex) {
                log.error("Refresh token subject is not numeric type: {}", userId.getClass().getSimpleName());
                throw new InvalidTokenException(String.format(
                        SecurityExceptionMP.INVALID_TOKEN_SUBJECT, ex.getClass().getSimpleName()));
            }
            log.debug("refresh token user subject is: {}", userId);
            DefaultUserInfoResponseDto userDto = userCrudService.findById(id);
            log.info("user with id {} was found", id);

            Date refreshTokenExpiration = claims.getExpiration();
            Instant expirationInstant = refreshTokenExpiration.toInstant();
            Instant nowInstant = Instant.now();
            long hoursUntilExpiration = ChronoUnit.HOURS.between(nowInstant, expirationInstant);

            if (hoursUntilExpiration < 23) {
                log.info("access and refresh tokens will be generated");
                return tokenManager.generateDefaultTokens(userDto);
            } else {
                Map<String, Object> defaultClaims = tokenManager.generateDefaultClaims(userDto);
                String newAccessToken = tokenManager.generateSpecialToken(
                        String.valueOf(userDto.id()),
                        defaultClaims,
                        TokenType.ACCESS,
                        tokensPropertiesDispatcher.getAccessTokenAvailableValidityPeriodInSec(),
                        applicationSecretKeysHolder.getJwtAccessSecretKey()
                );
                log.info("new access token was generated");
                return new TokenDto(refreshToken, newAccessToken);
            }
        } else {
            throw new InvalidTokenException(
                    String.format(
                            SecurityExceptionMP.INVALID_TOKEN
                    )
            );
        }
    }
}
