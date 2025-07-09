package com.best_store.right_bite.security.managment;

import com.best_store.right_bite.security.dto.TokenDto;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Map;

public interface TokenManager {
    TokenDto generateToken(@NonNull String userId, @Nullable Map<String, Object> claims);
}
