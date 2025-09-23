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
import io.jsonwebtoken.Claims;
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

/**
 * A custom implementation of {@link OncePerRequestFilter} that processes incoming HTTP requests
 * for JWT-based authentication and authorization. Ensures all security checks related to JWT tokens
 * are handled once per request.
 *
 * <ul>
 *     <li>Validates the JWT token from the request header.</li>
 *     <li>Checks if the token is revoked using {@link RevokeTokenService}.</li>
 *     <li>Determines the token type and extracts user details for authentication.</li>
 *     <li>Sets the {@link Authentication} object in the {@link SecurityContextHolder}.</li>
 * </ul>
 * <p>
 * Handles invalid tokens and sends appropriate unauthorized responses.
 * <p>
 * Usage:
 * Annotated with {@link Component} for Spring to manage as a bean.
 *
 * @author Ihor Murashko
 * @see JwtProvider
 * @see ClaimsProvider
 * @see RevokeTokenService
 * @see TokenType
 * @see TokenClaimsConstants
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultOncePerRequestFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final ClaimsProvider claimsProvider;
    private final RevokeTokenService revokedTokenService;

    /**
     * Processes each HTTP request and applies authentication logic by validating
     * JWT tokens and setting the appropriate authentication context.
     * <p>
     * Handles token validation, revocation checks, and token type verification.
     * If the token is valid, an authentication object is created and set into
     * the {@link SecurityContextHolder}. If token validation fails, an appropriate
     * response with an error message is issued.
     * </p>
     *
     * @param request     the {@link HttpServletRequest} containing client request details.
     * @param response    the {@link HttpServletResponse} to modify in case of errors.
     * @param filterChain the {@link FilterChain} to proceed with the rest of the filter processing.
     * @throws ServletException in case of request processing errors.
     * @throws IOException      in case of input/output errors.
     * @see SecurityContextHolder
     * @see OncePerRequestFilter#doFilterInternal(HttpServletRequest, HttpServletResponse, FilterChain)
     * @see JwtProvider#validateToken(String)
     */
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

                    if (tokenType == null || !(tokenType.equalsIgnoreCase(TokenType.ACCESS.name()))) {
                        log.debug("Request with refreshed token {}", token);
                        throw new InvalidTokenTypeException(String.format(
                                SecurityExceptionMessageProvider.INVALID_TOKEN_TYPE, tokenType
                        ));
                    }

                    String id = claimsProvider.extractClaimFromToken(token,
                            Claims::getSubject);
                    log.debug("User id is {}", id);
                    String email = claimsProvider.extractClaimFromToken(token,
                            claims -> claims.get(
                                    TokenClaimsConstants.USERNAME_CLAIM, String.class));
                    log.debug("Username is {}", email);
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