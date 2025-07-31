package com.best_store.right_bite.service.auth.update.password;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.auth.RandomPasswordGenerator;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordEncoder passwordEncoder;
    private final UserCrudService userCrudService;

    @Override
    public void resetPassword(@NonNull String email) {
        String adaptedEmail = UserFieldAdapter.toLower(email);
        if (!userCrudService.isEmailExist(adaptedEmail)) {
            throw new UserNotFoundException(
                    String.format(
                            ExceptionMessageProvider.USER_EMAIL_NOT_FOUND, adaptedEmail
                    )
            );
        }
        String newEncodedPassword = passwordEncoder.encode(RandomPasswordGenerator.generatePassword());
        userCrudService.resetPasswordByEmail(adaptedEmail, newEncodedPassword);
        log.info("Reset password for email {}", adaptedEmail);
        //todo: send async letter
        //todo: how often could you reset password
    }
}
