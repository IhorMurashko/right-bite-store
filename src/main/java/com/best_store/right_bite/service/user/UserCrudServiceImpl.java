package com.best_store.right_bite.service.user;


import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.user.UserRepository;
import com.best_store.right_bite.util.user.UserFieldAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserCrudServiceImpl implements UserCrudService {

    private final UserRepository userRepository;

    @Override
    public User findByEmail(@NonNull String email) {
        log.info("Find user by email: {}", email);
        return userRepository.findByEmail(UserFieldAdapter.toLower(email))
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ExceptionMessageProvider.USER_EMAIL_NOT_FOUND, email)
                ));
    }

    @Override
    public User findById(@NonNull Long id) {
        log.info("Find user by id: {}", id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ExceptionMessageProvider.USER_ID_NOT_FOUND, id)
                ));
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    @Override
    public User save(@NonNull User user) {
        log.info("Save user with email: {}", user.getEmail());
        return userRepository.save(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    @Override
    public void deleteById(@NonNull long id) {
        User user = findById(id);
        log.warn("Delete user by id: {}", id);
        userRepository.delete(user);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    @Override
    public void deleteByEmail(@NonNull String email) {
        User user = findByEmail(email);
        log.warn("Delete user by email: {}", email);
        userRepository.delete(user);
    }

    @Override
    public boolean isEmailExist(@NonNull String email) {
        log.debug("Check if user with email: {} exists", email);
        return userRepository.existsByEmail(UserFieldAdapter.toLower(email));
    }

    @Override
    public boolean isUserExistById(@NonNull long id) {
        log.debug("Check if user with id: {} exists", id);
        return userRepository.existsById(id);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,
            propagation = Propagation.REQUIRED)
    @Override
    public void resetPasswordByEmail(@NonNull String email, @NonNull String newEncodedPassword) {
        userRepository.updateUserPassword(UserFieldAdapter.toLower(email), newEncodedPassword);
        log.info("Reset user by email: {}", email);
    }
}