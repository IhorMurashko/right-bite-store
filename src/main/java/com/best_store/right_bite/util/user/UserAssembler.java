package com.best_store.right_bite.util.user;

import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.role.RoleExceptionMessageProvider;
import com.best_store.right_bite.exception.role.RoleNotFoundException;
import com.best_store.right_bite.model.auth.AuthProvider;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.role.RoleRepository;
import com.best_store.right_bite.security.constant.GoogleCredentialsConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;


@Component
@RequiredArgsConstructor
@Slf4j
public class UserAssembler {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public User create(@NonNull RegistrationCredentialsDto credentials) {
        String encodedPassword = passwordEncoder.encode(credentials.password());
        log.debug("Password encoded successfully");
        Role role = getRole(RoleName.ROLE_USER);
        return new User(UserFieldAdapter.toLower(credentials.email()), encodedPassword,
                true, true
                , true, true,
                Set.of(role), AuthProvider.LOCAL, null);
    }

    public User create(@NonNull OAuth2User oAuth2User) {
        String email = UserFieldAdapter.toLower(Objects.requireNonNull(
                oAuth2User.getAttribute(GoogleCredentialsConstants.EMAIL)));

        Role role = getRole(RoleName.ROLE_USER);

        User user = new User(
                email, null,
                true, true
                , true, true,
                Set.of(role), AuthProvider.GOOGLE, null);

        String firstName = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.FIRST_NAME);
        String lastName = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.LAST_NAME);
        String imageUrl = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.PICTURE);
        String oauthId = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.OAUTH_ID);

        if (firstName != null && !firstName.isBlank()) {
            log.debug("user first name was set {}", firstName);
            user.setFirstName(firstName);
        }
        if (lastName != null && !lastName.isBlank()) {
            log.debug("user last name was set {}", lastName);
            user.setLastName(lastName);
        }
        if (imageUrl != null && !imageUrl.isBlank()) {
            log.debug("user image was set {}", imageUrl);
            user.setImageUrl(imageUrl);
        }
        if (oauthId != null && !oauthId.isBlank()) {
            log.debug("user oauthId was set {}", oauthId);
            user.setOauthId(oauthId);
        }
        return user;
    }

    private Role getRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(
                                String.format(
                                        RoleExceptionMessageProvider.ROLE_NOT_FOUND, roleName
                                )
                        )
                );
    }
}
