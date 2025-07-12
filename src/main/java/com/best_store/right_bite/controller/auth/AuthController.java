package com.best_store.right_bite.controller.auth;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.security.blackListTokenCache.RevokeTokenService;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.service.auth.login.AuthenticationService;
import com.best_store.right_bite.service.auth.registration.RegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final RevokeTokenService revokeTokenService;

    @PreAuthorize("isAnonymous()")
    @PostMapping("/sing-up")
    public ResponseEntity<HttpStatus> singUp(@RequestBody RegistrationCredentialsDto credentialsDto) {
        registrationService.registration(credentialsDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/sing-in")
    public ResponseEntity<TokenDto> singIn(@RequestBody AuthRequest authRequest) {
        TokenDto tokenDto = authenticationService.authenticate(authRequest);
        return new ResponseEntity<>(tokenDto, HttpStatus.OK);
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/google")
    public void login(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public ResponseEntity<HttpStatus> logout(TokenDto tokenDto) {
        revokeTokenService.revokeToken(tokenDto.accessToken(), tokenDto.refreshToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
