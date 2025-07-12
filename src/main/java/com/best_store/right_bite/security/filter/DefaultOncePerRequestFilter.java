package com.best_store.right_bite.security.filter;

import com.best_store.right_bite.security.blackListTokenCache.RevokeTokenService;
import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.constant.HeaderConstants;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.exception.InvalidTokenTypeException;
import com.best_store.right_bite.security.exception.SecurityExceptionMessageProvider;
import com.best_store.right_bite.security.exception.TokenRevokedException;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.security.principal.JwtPrincipal;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultOncePerRequestFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ClaimsProvider claimsProvider;
    private final RevokeTokenService revokedTokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            if (request.getHeader(HeaderConstants.HEADER_AUTHENTICATION) != null) {

                final String token = jwtProvider.extractTokenFromHeader(request);

                if (token != null && jwtProvider.validateToken(token)) {
                    if (revokedTokenService.isTokenRevoked(token)) {
                        log.error("Token has benn revoked {}", token);
                        throw new TokenRevokedException(SecurityExceptionMessageProvider
                                .TOKEN_WAS_REVOKED);
                    }
                    String tokenType = claimsProvider.extractClaimFromToken(token,
                            claims -> claims.get(
                                    TokenClaimsConstants.TOKEN_TYPE_CLAIM, String.class)
                    );

                    if (tokenType != null && tokenType.equalsIgnoreCase(TokenType.REFRESH.name())) {
                        log.debug("Request with refreshed token {}", token);
                        throw new InvalidTokenTypeException(String.format(
                                SecurityExceptionMessageProvider.INVALID_TOKEN_TYPE, tokenType
                        ));
                    }

                    Long id = claimsProvider.extractClaimFromToken(token,
                            claims -> claims.get(
                                    TokenClaimsConstants.USER_ID_CLAIM, Long.class));
                    String email = claimsProvider.extractClaimFromToken(token,
                            claims -> claims.get(
                                    TokenClaimsConstants.USERNAME_CLAIM, String.class));

                    List<?> rawRoles = claimsProvider.extractClaimFromToken(token,
                            claims -> claims.get("roles", List.class));

                    List<String> roles = rawRoles.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .toList();

                    Set<GrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
                    log.debug("Authorities: {}", authorities);

                    Authentication auth = new UsernamePasswordAuthenticationToken(new JwtPrincipal(id, email),
                            null, authorities);
                    log.info("Set authentication in context holder for {}", id);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            log.warn("JWT exception on [{}]: {}", request.getRequestURI(), ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    String.format(SecurityExceptionMessageProvider.TOKEN_RESPONSE_TEMPLATE, ex.getMessage())
            );
        }
    }
}
