package com.best_store.right_bite.service.auth.update.password;

import com.best_store.right_bite.constant.notification.NotificationChannel;
import com.best_store.right_bite.constant.notification.NotificationType;
import com.best_store.right_bite.constant.notification.holder.letter.DefaultSubjectHolder;
import com.best_store.right_bite.constant.notification.holder.letter.ResetPasswordVariablesHolder;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.user.UserNotFoundException;
import com.best_store.right_bite.notification.data.core.DefaultNotification;
import com.best_store.right_bite.notification.data.payload.ResetPasswordContentPayload;
import com.best_store.right_bite.service.notificationService.dispatch.NotificationDispatcherService;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.auth.RandomPasswordGenerator;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetServiceImpl implements PasswordResetService {

    private final PasswordEncoder passwordEncoder;
    private final UserCrudService userCrudService;
    private final NotificationDispatcherService notificationDispatcherService;

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
