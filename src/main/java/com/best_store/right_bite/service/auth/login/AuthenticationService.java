package com.best_store.right_bite.service.auth.login;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.security.dto.TokenDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

@FunctionalInterface
public interface AuthenticationService {
    TokenDto authenticate(@NonNull @Valid AuthRequest authRequest);
}
