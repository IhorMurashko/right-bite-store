package com.best_store.right_bite.service.auth.login;

import com.best_store.right_bite.dto.auth.login.AuthRequest;
import com.best_store.right_bite.exception.exceptions.auth.CredentialsException;
import com.best_store.right_bite.exception.exceptions.auth.UserAccountIsNotAvailableException;
import com.best_store.right_bite.security.dto.TokenDto;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

/**
 * Authentication service interface defining the contract for user authentication.
 * <p>
 * Implementations must authenticate the user and return a valid {@link TokenDto} upon success.
 */
@FunctionalInterface
public interface AuthenticationService {

    /**
     * Authenticates the user with the provided credentials.
     *
     * @param authRequest the authentication request containing email and password
     * @return a token containing authentication claims
     * @throws CredentialsException               if credentials are invalid
     * @throws UserAccountIsNotAvailableException if the user's account is not in a valid state
     */
    TokenDto authenticate(@NonNull @Valid AuthRequest authRequest);
}

