package com.best_store.right_bite.security.filter;

import com.best_store.right_bite.security.blackListTokenCache.RevokeTokenService;
import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.exception.InvalidTokenTypeException;
import com.best_store.right_bite.security.exception.SecurityExceptionMessageProvider;
import com.best_store.right_bite.security.exception.TokenRevokedException;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.security.util.ApplicationSecretKeysHolder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class DefaultOncePerRequestFilterTest {

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private ClaimsProvider claimsProvider;
    @Mock
    private RevokeTokenService revokeTokenService;
    @Mock
    private ApplicationSecretKeysHolder applicationSecretKeysHolder;
    @InjectMocks
    private DefaultOncePerRequestFilter defaultOncePerRequestFilter;
    private final HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
    private final HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
    private final FilterChain filterChain = Mockito.mock(FilterChain.class);
    @Mock
    private PrintWriter printWriter;
    @Captor
    private ArgumentCaptor<HttpServletRequest> httpServletRequestArgumentCaptor;
    @Captor
    private ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    private String authorizationHeader;
    private String fullToken;
    private String token;
    private String access;
    private SecretKey key;

    @BeforeEach
    void setUp() {
        this.authorizationHeader = "Authorization";
        this.token = UUID.randomUUID().toString();
        this.fullToken = String.format("Bearer %s", token);
        this.access = TokenType.ACCESS.name();
        this.key = Keys.hmacShaKeyFor("test-secret-key-minimum-256-bits-required".getBytes(StandardCharsets.UTF_8));

    }

    @Test
    void doFilterChainWithoutControl_when_authenticationHeaderIsNull() throws ServletException, IOException {
        doReturn(null).when(request).getHeader(authorizationHeader);

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verifyNoInteractions(jwtProvider, claimsProvider, revokeTokenService);
    }

    @Test
    void doFilterChainWithoutControl_when_authenticationHeaderIsPresentAndHasNullValue() throws ServletException, IOException {
        doReturn("").when(request).getHeader(authorizationHeader);
        doReturn(null).when(jwtProvider).extractTokenFromHeader(request);

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).extractTokenFromHeader(httpServletRequestArgumentCaptor.capture());
        assertEquals(request, httpServletRequestArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).extractTokenFromHeader(request);
        verifyNoMoreInteractions(jwtProvider);
        verifyNoInteractions(claimsProvider, revokeTokenService);
    }


    @Test
    void doFilterChainWithoutControl_when_authenticationHeaderIsPresentAndHasInvalidToken() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();

        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(false).when(jwtProvider).validateToken(token, key);

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).extractTokenFromHeader(httpServletRequestArgumentCaptor.capture());
        assertEquals(request, httpServletRequestArgumentCaptor.getValue());

        verify(jwtProvider, times(1)).validateToken(stringArgumentCaptor.capture(), eq(key));
        assertEquals(token, stringArgumentCaptor.getValue());

        verifyNoMoreInteractions(jwtProvider);
        verifyNoInteractions(claimsProvider, revokeTokenService);
    }


    @Test
    void throwTokenRevokedException_when_tokenWasRevoked() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(true).when(revokeTokenService).isTokenRevoked(token);


        TokenRevokedException exception = assertThrows(TokenRevokedException.class, () ->
                defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain));

        assertEquals(SecurityExceptionMessageProvider.TOKEN_WAS_REVOKED, exception.getMessage());
        verify(jwtProvider, times(1)).extractTokenFromHeader(httpServletRequestArgumentCaptor.capture());
        assertEquals(request, httpServletRequestArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(stringArgumentCaptor.capture(), eq(key));
        assertEquals(token, stringArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verifyNoMoreInteractions(jwtProvider, revokeTokenService);
        verifyNoInteractions(claimsProvider);
    }

    @Test
    void throwInvalidTokenTypeException_when_tokenTypeIsNull() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(false).when(revokeTokenService).isTokenRevoked(token);
        doReturn(null).when(claimsProvider).extractClaimFromToken(eq(token), eq(key), any());

        InvalidTokenTypeException exception = assertThrows(InvalidTokenTypeException.class,
                () -> defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain));

        assertEquals(String.format(SecurityExceptionMessageProvider.INVALID_TOKEN_TYPE, null), exception.getMessage());
        verify(jwtProvider, times(1)).extractTokenFromHeader(httpServletRequestArgumentCaptor.capture());
        assertEquals(request, httpServletRequestArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(stringArgumentCaptor.capture(), eq(key));
        assertEquals(token, stringArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verify(claimsProvider, times(1)).extractClaimFromToken(eq(token), eq(key), any());
        verifyNoMoreInteractions(jwtProvider, revokeTokenService, claimsProvider);
    }

    @Test
    void throwInvalidTokenTypeException_when_tokenTypeIsNotAccess() throws ServletException, IOException {
        String refresh = TokenType.REFRESH.name();
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(false).when(revokeTokenService).isTokenRevoked(token);
        doReturn(refresh).when(claimsProvider).extractClaimFromToken(eq(token), any(), any());

        InvalidTokenTypeException exception = assertThrows(InvalidTokenTypeException.class,
                () -> defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain));

        assertEquals(String.format(SecurityExceptionMessageProvider.INVALID_TOKEN_TYPE, refresh), exception.getMessage());
        verify(jwtProvider, times(1)).extractTokenFromHeader(httpServletRequestArgumentCaptor.capture());
        assertEquals(request, httpServletRequestArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(stringArgumentCaptor.capture(), eq(key));
        assertEquals(token, stringArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verify(claimsProvider, times(1)).extractClaimFromToken(eq(token), eq(key), any());
        verifyNoMoreInteractions(jwtProvider, revokeTokenService, claimsProvider);
    }

    @Test
    void throwMalformedJwtException_when_tokenDoesNotContainsIdClaim() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(false).when(revokeTokenService).isTokenRevoked(token);
        doAnswer(invocation -> {
            Function<Claims, ?> function = invocation.getArgument(2);
            Claims mockClaims = mock(Claims.class);
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.TOKEN_TYPE_CLAIM), eq(String.class)))
                    .thenReturn(access);
            lenient().when(mockClaims.getSubject())
                    .thenThrow(new MalformedJwtException("invalid id claim"));
            return function.apply(mockClaims);
        }).when(claimsProvider).extractClaimFromToken(eq(token), eq(key), any());
        doReturn(printWriter).when(response).getWriter();

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(integerArgumentCaptor.capture());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, integerArgumentCaptor.getValue());
        verify(response, times(1)).setContentType(stringArgumentCaptor.capture());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, stringArgumentCaptor.getValue());
        verify(response, times(1)).getWriter();
        verify(jwtProvider, times(1)).extractTokenFromHeader(request);
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verify(claimsProvider, times(2)).extractClaimFromToken(eq(token), eq(key), any());
        verifyNoMoreInteractions(jwtProvider, revokeTokenService, claimsProvider);
    }

    @Test
    void throwMalformedJwtException_when_tokenDoesNotContainsEmailClaim() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(false).when(revokeTokenService).isTokenRevoked(token);
        doAnswer(invocation -> {
            Function<Claims, ?> function = invocation.getArgument(2);
            Claims mockClaims = mock(Claims.class);
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.TOKEN_TYPE_CLAIM), eq(String.class)))
                    .thenReturn(access);
            lenient().when(mockClaims.getSubject())
                    .thenReturn("1");
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.USERNAME_CLAIM), eq(String.class)))
                    .thenThrow(new MalformedJwtException("invalid email claim"));
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.ROLES_CLAIM), eq(List.class)))
                    .thenReturn(List.of("ROLE_USER"));
            return function.apply(mockClaims);
        }).when(claimsProvider).extractClaimFromToken(eq(token), eq(key), any());
        doReturn(printWriter).when(response).getWriter();

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(integerArgumentCaptor.capture());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, integerArgumentCaptor.getValue());
        verify(response, times(1)).setContentType(stringArgumentCaptor.capture());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, stringArgumentCaptor.getValue());
        verify(response, times(1)).getWriter();
        verify(jwtProvider, times(1)).extractTokenFromHeader(request);
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verify(claimsProvider, times(3)).extractClaimFromToken(eq(token), eq(key), any());
        verifyNoMoreInteractions(jwtProvider, revokeTokenService, claimsProvider);
    }

    @Test
    void throwMalformedJwtException_when_tokenDoesNotContainsRolesClaim() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(false).when(revokeTokenService).isTokenRevoked(token);
        doAnswer(invocation -> {
            Function<Claims, ?> function = invocation.getArgument(2);
            Claims mockClaims = mock(Claims.class);
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.TOKEN_TYPE_CLAIM), eq(String.class)))
                    .thenReturn(access);
            lenient().when(mockClaims.getSubject())
                    .thenReturn("1");
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.USERNAME_CLAIM), eq(String.class)))
                    .thenReturn("username");
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.ROLES_CLAIM), eq(List.class)))
                    .thenThrow(new MalformedJwtException("invalid role claim"));
            return function.apply(mockClaims);
        }).when(claimsProvider).extractClaimFromToken(eq(token), eq(key), any());
        doReturn(printWriter).when(response).getWriter();

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(integerArgumentCaptor.capture());
        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, integerArgumentCaptor.getValue());
        verify(response, times(1)).setContentType(stringArgumentCaptor.capture());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, stringArgumentCaptor.getValue());
        verify(response, times(1)).getWriter();
        verify(jwtProvider, times(1)).extractTokenFromHeader(request);
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verify(claimsProvider, times(4)).extractClaimFromToken(eq(token), eq(key), any());
        verifyNoMoreInteractions(jwtProvider, revokeTokenService, claimsProvider);
    }

    @Test
    void doFilterChainSuccessfully_when_tokenIsValid() throws ServletException, IOException {
        doReturn(key).when(applicationSecretKeysHolder).getJwtAccessSecretKey();
        doReturn(fullToken).when(request).getHeader(authorizationHeader);
        doReturn(token).when(jwtProvider).extractTokenFromHeader(request);
        doReturn(true).when(jwtProvider).validateToken(token, key);
        doReturn(false).when(revokeTokenService).isTokenRevoked(token);
        doAnswer(invocation -> {
            Function<Claims, ?> function = invocation.getArgument(2);
            Claims mockClaims = mock(Claims.class);
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.TOKEN_TYPE_CLAIM), eq(String.class)))
                    .thenReturn(access);
            lenient().when(mockClaims.getSubject())
                    .thenReturn("1");
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.USERNAME_CLAIM), eq(String.class)))
                    .thenReturn("username");
            lenient().when(mockClaims.get(eq(TokenClaimsConstants.ROLES_CLAIM), eq(List.class)))
                    .thenReturn(List.of("ROLE_USER"));
            return function.apply(mockClaims);
        }).when(claimsProvider).extractClaimFromToken(eq(token), eq(key), any());

        defaultOncePerRequestFilter.doFilterInternal(request, response, filterChain);

        verify(jwtProvider, times(1)).extractTokenFromHeader(httpServletRequestArgumentCaptor.capture());
        assertEquals(request, httpServletRequestArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).validateToken(stringArgumentCaptor.capture(), eq(key));
        assertEquals(token, stringArgumentCaptor.getValue());
        verify(jwtProvider, times(1)).extractTokenFromHeader(request);
        verify(jwtProvider, times(1)).validateToken(token, key);
        verify(revokeTokenService, times(1)).isTokenRevoked(token);
        verify(claimsProvider, times(4)).extractClaimFromToken(eq(token), eq(key), any());
        verifyNoMoreInteractions(jwtProvider, revokeTokenService, claimsProvider);
    }
}