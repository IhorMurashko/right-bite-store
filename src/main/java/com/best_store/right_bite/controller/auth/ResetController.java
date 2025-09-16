package com.best_store.right_bite.controller.auth;

import com.best_store.right_bite.dto.auth.reset.PasswordResetRequest;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.service.auth.update.password.PasswordResetService;
import com.best_store.right_bite.service.auth.update.token.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Updatable controller",
        description = "forget password, get new refresh/access tokens operation")
@RestController
@RequestMapping("/api/v1/reset")
@RequiredArgsConstructor
@Validated
@PreAuthorize("isAnonymous()")
public class ResetController {

    private final PasswordResetService passwordResetService;
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "reset password",
            description = "get new password to user email. request can't contains any jwt token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "user email",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PasswordResetRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "client request error"),
                    @ApiResponse(responseCode = "500", description = "server error")
            })
    @PostMapping("/password")
    public ResponseEntity<HttpStatus> resetPasswordByEmail(@NonNull @RequestBody @Valid PasswordResetRequest passwordResetRequest) {
        passwordResetService.resetPassword(passwordResetRequest.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "refresh token",
            description = "get new access and refresh, if needed, tokens. request must contains refresh jwt token.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "refresh and old access token",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TokenDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "400", description = "client request error"),
                    @ApiResponse(responseCode = "500", description = "server error")
            })
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshToken(@NonNull @RequestBody TokenDto tokenDto) {
        TokenDto refreshed = refreshTokenService.refreshToken(tokenDto);
        return new ResponseEntity<>(refreshed, HttpStatus.OK);
    }
}
