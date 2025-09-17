package com.best_store.right_bite.service.auth.update.token;

import com.best_store.right_bite.security.dto.TokenDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

@FunctionalInterface
public interface RefreshTokenService {
    TokenDto refreshToken(@NonNull @Valid TokenDto tokenDto);
}
