package com.best_store.right_bite.service.auth.login;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.exception.auth.CredentialsException;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.auth.UserAccountIsNotAvailableException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.managment.TokenManager;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
/**
 * Email/password-based implementation of the {@link AuthenticationService}.
 * <p>
 * Validates user credentials and account status, then generates an authentication token.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmailPasswordAuthenticationServiceImpl implements AuthenticationService {

    private final UserCrudService userCrudService;
    private final PasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;


    /**
     * Authenticates a user by verifying their email and password,
     * and checking account status flags.
     *
     * @param authRequest the authentication request
     * @return a signed JWT token with default claims
     * @throws CredentialsException if the password is invalid
     * @throws UserAccountIsNotAvailableException if the user's account is expired, locked, or disabled
     */
    @Override
    public TokenDto authenticate(@NonNull @Valid AuthRequest authRequest) {
        User user = userCrudService.findByEmail(authRequest.email());
        if (!passwordEncoder.matches(authRequest.password(), user.getPassword())) {
            throw new CredentialsException(ExceptionMessageProvider
                    .PASSWORDS_DONT_MATCH);
        }

        if (!user.isAccountNonExpired()
                || !user.isAccountNonLocked()
                || !user.isCredentialsNonExpired()
                || !user.isEnabled()) {
            log.warn("account non expired {}", user.isAccountNonExpired());
            log.warn("account non locked {}", user.isAccountNonLocked());
            log.warn("account credentials non expired {}", user.isCredentialsNonExpired());
            log.warn("account credentials is enabled {}", user.isEnabled());
            log.warn("account is expired for user: {}", user.getEmail());
            throw new UserAccountIsNotAvailableException(
                    ExceptionMessageProvider.USER_ACCOUNT_IS_EXPIRED);
        }
        return tokenManager.generateDefaultClaimsToken(user);
    }
}
