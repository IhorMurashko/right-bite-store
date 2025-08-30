package com.best_store.right_bite.service.user.crud;


import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of {@link UserCrudService} backed by {@link UserRepository}.
 *
 * <p>Handles transactional persistence operations for {@link User} entities.
 * Normalizes email fields and ensures proper exception handling.</p>
 *
 * <p>Provides audit-level logging for all critical operations.</p>
 *
 * @see UserRepository
 * @see UserFieldAdapter
 * @see UserNotFoundException
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserCrudServiceImpl implements UserCrudService {

    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findByEmail(@NonNull String email) {
        log.info("Find user by email: {}", email);
        return userRepository.findByEmail(UserFieldAdapter.toLower(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findById(@NonNull Long id) {
        log.info("Find user by id: {}", id);
        return userRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public User save(@NonNull User user) {
        log.info("Save user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(@NonNull Long id) {
        User user = findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ExceptionMessageProvider.USER_ID_NOT_FOUND, id)
                ));

        log.warn("Delete user by id: {}", id);
        userRepository.delete(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void deleteByEmail(@NonNull String email) {
        User user = findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ExceptionMessageProvider.USER_EMAIL_NOT_FOUND, email)
                ));
        log.warn("Delete user by email: {}", email);
        userRepository.delete(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmailExist(@NonNull String email) {
        log.debug("Check if user with email: {} exists", email);
        return userRepository.existsByEmail(UserFieldAdapter.toLower(email));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isUserExistById(@NonNull Long id) {
        log.debug("Check if user with id: {} exists", id);
        return userRepository.existsById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void resetPasswordByEmail(@NonNull String email, @NonNull String newEncodedPassword) {
        userRepository.updateUserPassword(UserFieldAdapter.toLower(email), newEncodedPassword);
        log.info("Reset user by email: {}", email);
    }
}
