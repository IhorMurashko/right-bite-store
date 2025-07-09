package com.best_store.right_bite.security.filter;

import com.best_store.right_bite.security.blackListTokenCache.RevokeTokenService;
import com.best_store.right_bite.security.claims.ClaimsProvider;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.exception.SecurityExceptionMessageProvider;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            final String token = extractToken(request);

            if (token != null && jwtProvider.validateToken(token)) {
                if (revokedTokenService.isTokenRevoked(token)) {
                    log.error("Token has benn revoked {}", token);
                    throw new JwtException(SecurityExceptionMessageProvider
                            .TOKEN_WAS_REVOKED);
                }
                String tokenType = claimsProvider.extractClaimFromToken(token,
                        claims -> claims.get(
                                TokenClaimsConstants.TOKEN_TYPE_CLAIM, String.class)
                );

                if (tokenType != null && tokenType.equalsIgnoreCase(TokenType.REFRESH.name())) {
                    log.debug("Request with refreshed token {}", token);
                    throw new JwtException(String.format(
                            SecurityExceptionMessageProvider.INVALID_TOKEN_TYPE, tokenType
                    ));
                }

                Long id = claimsProvider.extractClaimFromToken(token,
                        claims -> claims.get(
                                TokenClaimsConstants.USER_ID_CLAIM, Long.class));

                List<String> roles = claimsProvider.extractClaimFromToken(token,
                        claims -> claims.get(TokenClaimsConstants.ROLES_CLAIM, List.class));

                Set<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
                log.debug("Authorities: {}", authorities);

                Authentication auth = new UsernamePasswordAuthenticationToken(id, null, authorities);
                log.info("Set authentication in context holder for {}", id);
                SecurityContextHolder.getContext().setAuthentication(auth);

            }
        } catch (JwtException ex) {
            //todo: constant variables
            log.warn("JWT exception: {}", ex.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + ex.getMessage() + "\"}");
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     * <p>
     * The method expects the header to begin with the string "Bearer ".
     * If the header is absent or does not follow this format, a warning is logged and {@code null} is returned.
     *
     * @param request the HttpServletRequest from which the token is to be extracted
     * @return the JWT token string if present and well-formed; {@code null} otherwise
     */
    private String extractToken(HttpServletRequest request) {
        final String requestToken = request.getHeader("Authorization");

        if (requestToken == null) {
            log.warn("There is no Authorization header");
            return null;
        }
        if (!requestToken.startsWith("Bearer ")) {
            log.warn("Token does not begin with Bearer");
            return null;
        }
        return requestToken.substring(7);
    }
}
