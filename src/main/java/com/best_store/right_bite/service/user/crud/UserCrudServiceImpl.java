package com.best_store.right_bite.service.user.crud;


import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.mapper.user.DefaultUserInfoDtoMapper;
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
    private final DefaultUserInfoDtoMapper defaultUserMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultUserInfoResponseDto findByEmail(@NonNull String email) {
        log.info("Find user by email: {}", email);
        User user = userRepository.findByEmail(UserFieldAdapter.toLower(email)).orElseThrow(() -> new UserNotFoundException(
                String.format(ExceptionMessageProvider.USER_EMAIL_NOT_FOUND, email)
        ));
        return defaultUserMapper.toDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DefaultUserInfoResponseDto findById(@NonNull Long id) {
        log.info("Find user by id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ExceptionMessageProvider.USER_ID_NOT_FOUND, id)));
        return defaultUserMapper.toDTO(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public DefaultUserInfoResponseDto save(@NonNull User user) {
        log.info("Save user with email: {}", user.getEmail());
        User saved = userRepository.save(user);
        return defaultUserMapper.toDTO(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(@NonNull Long id) {
        if (isUserExistById(id)) {
            log.warn("Delete user by id: {}", id);
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(String.format(ExceptionMessageProvider.USER_ID_NOT_FOUND, id));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public void deleteByEmail(@NonNull String email) {
        if (isEmailExist(email)) {
            log.warn("Delete user by email: {}", email);
            userRepository.deleteByEmail(UserFieldAdapter.toLower(email));
        } else {
            throw new UserNotFoundException(String.format(ExceptionMessageProvider.USER_EMAIL_NOT_FOUND, email));
        }
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
