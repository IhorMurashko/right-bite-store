package com.best_store.right_bite.controller.auth;

import com.best_store.right_bite.dto.auth.reset.PasswordResetRequest;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.service.auth.update.password.PasswordResetService;
import com.best_store.right_bite.service.auth.update.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reset")
@RequiredArgsConstructor
public class ResetController {

    private final PasswordResetService passwordResetService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/password")
    public ResponseEntity<HttpStatus> resetPasswordByEmail(@NonNull @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordResetService.resetPassword(passwordResetRequest.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenDto> refreshToken(@NonNull @RequestBody TokenDto tokenDto) {
        TokenDto refreshed = refreshTokenService.refreshToken(tokenDto);
        return new ResponseEntity<>(refreshed, HttpStatus.OK);
    }
}
