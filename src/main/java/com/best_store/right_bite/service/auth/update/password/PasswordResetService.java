package com.best_store.right_bite.service.auth.update.password;

import org.springframework.lang.NonNull;

public interface PasswordResetService {
    void resetPassword(@NonNull String email);
}
