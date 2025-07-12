package com.best_store.right_bite.dto.auth.login;

import jakarta.validation.constraints.NotBlank;
import org.springframework.lang.NonNull;

public record AuthRequest(
        @NonNull
        @NotBlank
        String email,
        @NonNull
        @NotBlank
        String password
) {
}
