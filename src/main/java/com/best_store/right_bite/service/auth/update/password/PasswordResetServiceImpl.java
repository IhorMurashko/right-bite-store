package com.best_store.right_bite.service.auth.update.password;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.constant.notification.holder.letter.ResetPasswordVariablesHolder;
import com.best_store.right_bite.exception.exceptions.user.UserNotFoundException;
import com.best_store.right_bite.exception.messageProvider.UserExceptionMP;
import com.best_store.right_bite.notification.data.core.DefaultNotification;
import com.best_store.right_bite.notification.data.payload.ResetPasswordContentPayload;
import com.best_store.right_bite.service.notificationService.dispatch.NotificationDispatcherService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.auth.RandomPasswordGenerator;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link PasswordResetService} for resetting user passwords and sending
 * the new password via email notification.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordEncoder passwordEncoder;
    private final UserCrudService userCrudService;
    private final NotificationDispatcherService notificationDispatcherService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetPassword(@NotNull String email) {
        String adaptedEmail = UserFieldAdapter.toLower(email);
        if (!userCrudService.isEmailExist(adaptedEmail)) {
            throw new UserNotFoundException(
                    String.format(
                            UserExceptionMP.USER_EMAIL_NOT_FOUND, adaptedEmail
                    )
            );
        }
        String newPassword = RandomPasswordGenerator.generatePassword();
        log.debug("New password: {}", newPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        userCrudService.resetPasswordByEmail(adaptedEmail, encodedPassword);
        log.info("Reset password for email {}", adaptedEmail);
        log.info("New encoded password: {}", encodedPassword);
        DefaultNotification notification = new DefaultNotification(
                NotificationType.RESETTING_PASSWORD_NOTIFICATION,
                NotificationChannel.EMAIL,
                List.of(email),
                ResetPasswordVariablesHolder.EMAIL_SUBJECT,
                ResetPasswordVariablesHolder.BODY_TITLE,
                new ResetPasswordContentPayload(newPassword)
        );
        notificationDispatcherService.send(notification);
    }
}