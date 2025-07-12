package com.best_store.right_bite.controller.auth;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.security.blackListTokenCache.RevokeTokenService;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.jwtProvider.JwtProvider;
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

@Tag(name = "Authentication controller",
        description = "registration, login, logout operations")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final RevokeTokenService revokeTokenService;
    private final JwtProvider jwtProvider;

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

    @Operation(
            summary = "sing in",
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

    @Operation(
            summary = "sing in",
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
