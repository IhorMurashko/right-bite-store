package com.best_store.right_bite.service.auth.registration;

import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.CredentialsException;
import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.model.auth.AuthProvider;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.service.user.UserCrudService;
import com.best_store.right_bite.util.UserFieldAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final UserCrudService userCrudService;

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

        String encodedPassword = passwordEncoder.encode(credentials.password());
        log.debug("Password encoded successfully");
        //todo: repository
        Set<Role> defaultRoles = Set.of(new Role(RoleName.ROLE_USER));


        User user = new User(
                email, encodedPassword,
                true, true
                , true, true,
                defaultRoles, AuthProvider.LOCAL, null);

        userCrudService.save(user);
        log.info("User {} saved successfully", email);
    }
}
