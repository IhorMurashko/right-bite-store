package com.best_store.right_bite.security.managment;

import com.best_store.right_bite.model.user.AbstractUser;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import org.springframework.lang.NonNull;

import java.util.Map;

public interface TokenManager {
    String generateToken(@NonNull String subject, @NonNull Map<String, Object> claims,
                         @NonNull TokenType tokenType, @NonNull Long lifePeriodTokenInSecond);

    TokenDto generateDefaultClaimsToken(@NonNull AbstractUser user);
}
