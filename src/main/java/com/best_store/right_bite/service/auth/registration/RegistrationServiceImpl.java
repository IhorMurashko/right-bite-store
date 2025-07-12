package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.auth.CredentialsException;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.service.notificationService.NotificationDispatcherService;
import com.best_store.right_bite.service.user.UserCrudService;
import com.best_store.right_bite.util.user.UserAssembler;
import com.best_store.right_bite.util.user.UserFieldAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final UserCrudService userCrudService;
    private final UserAssembler userAssembler;
    private final NotificationDispatcherService notificationDispatcherService;

    @Override
    public void registration(@NonNull @Valid RegistrationCredentialsDto credentials) {

        String email = UserFieldAdapter.toLower(credentials.email());

        if (userCrudService.isEmailExist(email)) {
            log.warn("Attempt to register already existing email: {}", email);
            throw new CredentialsException(
                    String.format(
                            ExceptionMessageProvider.EMAIL_ALREADY_EXIST, email
                    ));
        }

        if (!Objects.equals(credentials.password(), credentials.confirmationPassword())) {
            log.error("Passwords do not match");
            throw new CredentialsException(
                    ExceptionMessageProvider.PASSWORDS_DONT_MATCH);
        }

        User user = userAssembler.create(credentials);

        userCrudService.save(user);
        log.info("User {} saved successfully", email);
    }
}
