package com.best_store.right_bite.controller.auth;

import com.best_store.right_bite.dto.auth.reset.PasswordResetRequest;
import com.best_store.right_bite.service.auth.remindPassword.PasswordResetService;
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

    @PostMapping("/password")
    public ResponseEntity<HttpStatus> resetPasswordByEmail(@NonNull @RequestBody PasswordResetRequest passwordResetRequest) {
        passwordResetService.resetPassword(passwordResetRequest.email());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
