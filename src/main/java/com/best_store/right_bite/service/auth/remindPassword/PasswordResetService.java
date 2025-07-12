package com.best_store.right_bite.service.auth.remindPassword;

import org.springframework.lang.NonNull;

public interface PasswordResetService {
    void resetPassword(@NonNull String email);
}
