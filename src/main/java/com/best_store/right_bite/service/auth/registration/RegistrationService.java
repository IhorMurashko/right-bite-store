package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.auth.CredentialsException;
import jakarta.validation.Valid;
import org.springframework.lang.NonNull;

/**
 * Registration service responsible for registering new users.
 *
 * <p>Defines the contract for validating and creating user accounts
 * based on email and password credentials.</p>
 *
 * <p>Expected to be implemented with transactional, validation-aware logic
 * including duplicate email checking and password confirmation.</p>
 *
 * @author Ihor
 */
public interface RegistrationService {

    /**
     * Registers a new user account using provided credentials.
     *
     * @param credentials DTO containing user's email, password, and confirmation password.
     * @throws CredentialsException if email already exists or passwords do not match.
     */
    void registration(@NonNull @Valid RegistrationCredentialsDto credentials);
}
