package com.best_store.right_bite.dto.auth.login;

import com.best_store.right_bite.exception.messageProvider.AuthExceptionMP;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//todo: interface
public record AuthRequest(
        @NotNull(message = AuthExceptionMP.EMAIL_FIELD_CANT_BE_EMPTY_OR_NULL)
        @NotBlank(message = AuthExceptionMP.EMAIL_FIELD_CANT_BE_EMPTY_OR_NULL)
        String email,
        @NotNull(message = AuthExceptionMP.PASSWORD_FIELD_CANT_BE_EMPTY_OR_NULL)
        @NotBlank(message = AuthExceptionMP.PASSWORD_FIELD_CANT_BE_EMPTY_OR_NULL)
        String password
) {
}
