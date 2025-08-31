package com.best_store.right_bite.service.auth.update.token;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.auth.InvalidTokenException;
import com.best_store.right_bite.exception.auth.RefreshTokenAccessException;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.security.managment.TokenManager;
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

import javax.crypto.SecretKey;
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
    private final SecretKey key;
    private final TokensPropertiesDispatcher tokensPropertiesDispatcher;

    @Override
    public TokenDto refreshToken(@NonNull @Valid TokenDto tokenDto) {
        String refreshToken = tokenDto.refreshToken();


        if (jwtProvider.validateToken(refreshToken)) {
            String tokenType = claimsProvider.extractClaimFromToken(refreshToken, claims ->
                    claims.get(TokenClaimsConstants.TOKEN_TYPE_CLAIM, String.class));
            log.debug("refresh token type: {}", tokenType);
            if (!Objects.equals(tokenType, TokenType.REFRESH.name())) {
                log.error("Invalid refresh token for");
                throw new RefreshTokenAccessException(
                        String.format(
                                ExceptionMessageProvider.TOKEN_ACCESS_EXCEPTION, tokenType
                        )
                );
            }
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(refreshToken)
                    .getPayload();

//            String userId = claimsProvider.extractClaimFromToken(tokenType, Claims::getSubject);
            String userId = claims.getSubject();
            log.debug("refresh token user id: {}", userId);

            Long id = Long.parseLong(userId);
            User user = userCrudService.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(
                            String.format(ExceptionMessageProvider.USER_ID_NOT_FOUND, id)
                    ));
            log.info("user with id {} was found", id);


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
            throw new InvalidTokenException(
                    String.format(
                            ExceptionMessageProvider.INVALID_TOKEN
                    )
            );
        }
    }
}
