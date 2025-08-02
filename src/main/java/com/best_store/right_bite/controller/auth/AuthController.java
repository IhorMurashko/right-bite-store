package com.best_store.right_bite.controller.auth;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.auth.CredentialsException;
import com.best_store.right_bite.exception.auth.UserAccountIsNotAvailableException;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.security.blackListTokenCache.RevokeTokenService;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
import com.best_store.right_bite.security.oauht2.GoogleOAuthSuccessHandler;
import com.best_store.right_bite.service.auth.login.AuthenticationService;
import com.best_store.right_bite.service.auth.registration.RegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * REST controller responsible for authentication-related operations:
 * registration, login, and logout.
 *
 * <p>Exposes endpoints under the base path <code>/api/v1/auth</code>.</p>
 *
 * <p>Includes access control using {@link PreAuthorize} annotations to restrict access
 * based on authentication state.</p>
 *
 * <p>Relies on {@link RegistrationService}, {@link AuthenticationService},
 * {@link RevokeTokenService}, and {@link JwtProvider}.</p>
 *
 * <p>Documented with OpenAPI/Swagger annotations.</p>
 *
 * @author Ihor
 */
@Tag(
        name = "Authentication controller",
        description = "registration, login, logout operations"
)
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final RevokeTokenService revokeTokenService;
    private final JwtProvider jwtProvider;

    /**
     * Registers a new user using email and password.
     *
     * <p>Should be called without any JWT token in the request.
     * Only anonymous users are allowed.</p>
     *
     * <p>Returns 201 CREATED on success, or 400 BAD REQUEST on validation failure.</p>
     *
     * @param credentialsDto DTO with email, password, and confirmation password.
     * @return 201 Created if successful.
     */
    @Operation(
            summary = "registration",
            description = "registration using email and password. request can't contains any jwt token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "registration credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegistrationCredentialsDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "success"),
                    @ApiResponse(responseCode = "400", description = "validation error")
            })
    @PreAuthorize("isAnonymous()")
    @PostMapping("/sing-up")
    public ResponseEntity<HttpStatus> singUp(@RequestBody RegistrationCredentialsDto credentialsDto) {
        registrationService.registration(credentialsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Handles user sign-in using email and password.
     * <p>
     * This endpoint is intended for anonymous users only and must not be called with an existing JWT token.
     *
     * @param authRequest the authentication credentials (email and password)
     * @return a response containing generated access and refresh tokens
     * @throws CredentialsException               if password doesn't match
     * @throws UserNotFoundException              if user with given email is not found
     * @throws UserAccountIsNotAvailableException if the user account is expired, locked or disabled
     */
    @Operation(
            summary = "email-password login",
            description = "sign in using email and password. request can't contains any jwt token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "authentication credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = AuthRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(schema = @Schema(implementation = TokenDto.class))),
                    @ApiResponse(responseCode = "400", description = "validation error"),
                    @ApiResponse(responseCode = "404", description = "email wasn't found")
            })
    @PreAuthorize("isAnonymous()")
    @PostMapping("/sing-in")
    public ResponseEntity<TokenDto> singIn(@RequestBody AuthRequest authRequest) {
        TokenDto tokenDto = authenticationService.authenticate(authRequest);
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    /**
     * Initiates Google OAuth2 login flow by redirecting to Google's OAuth2 authorization endpoint.
     * <p>
     * This endpoint must be accessed anonymously (no existing JWT token).
     * On success, the user will be authenticated via OAuth2 and
     * the {@link GoogleOAuthSuccessHandler} will issue JWT tokens.
     *
     * @param response the HTTP response used to redirect the client
     * @throws IOException if redirection fails
     */
    @Operation(
            summary = "google sing in",
            description = "sign in using google. request can't contains any jwt token.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "success",
                            content = @Content(schema = @Schema(implementation = TokenDto.class))),
                    @ApiResponse(responseCode = "400", description = "validation error"),
                    @ApiResponse(responseCode = "404", description = "email wasn't found")
            })
    @PreAuthorize("isAnonymous()")
    @GetMapping("/google")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    /**
     * Logs out the current authenticated user by revoking the JWT token.
     *
     * <p>The request must contain a valid JWT token in the Authorization header.
     * The token is extracted and marked as revoked to prevent further use.</p>
     *
     * @param request the HTTP servlet request containing the Authorization header with JWT token
     * @return HTTP 204 No Content on successful logout
     */
    @Operation(
            summary = "logout",
            description = "logout. request must contains jwt token.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "no content")
            })
    @SecurityRequirement(name = "JWT")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/logout")
    public ResponseEntity<HttpStatus> logout(@NonNull HttpServletRequest request) {
        String token = jwtProvider.extractTokenFromHeader(request);
        revokeTokenService.revokeToken(token);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
