package com.best_store.right_bite.utils.user;

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

/**
 * Utility component for constructing {@link User} entities from various sources.
 *
 * <p>This assembler centralizes the logic of creating users from:
 * <ul>
 *   <li>Registration form input (email + password)</li>
 *   <li>OAuth2 provider data.sql (e.g. Google)</li>
 * </ul>
 *
 * <p>Encodes passwords, assigns default roles, and normalizes user fields (like email).</p>
 *
 * @author Ihor Murashko
 */
@Component
@RequiredArgsConstructor
@Slf4j
//todo: create interface
public class UserAssembler {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Creates a new {@link User} from registration credentials.
     *
     * <p>The password is encoded and the default {@code ROLE_USER} is assigned.</p>
     *
     * @param credentials DTO containing email and raw password
     * @return a new {@link User} entity ready for persistence
     */
    public User create(@NonNull RegistrationCredentialsDto credentials) {
        String encodedPassword = passwordEncoder.encode(credentials.password());
        log.debug("Password encoded successfully");

        Role role = getRole(RoleName.ROLE_USER);

        return new User(
                UserFieldAdapter.toLower(credentials.email()),
                encodedPassword,
                true, true, true, true,
                Set.of(role),
                AuthProvider.LOCAL,
                null
        );
    }

    /**
     * Creates a new {@link User} from an OAuth2 user object (e.g. from Google).
     *
     * <p>Email is extracted and normalized. OAuth2-specific data.sql like first name,
     * last name, image URL, and OAuth ID are set if present.</p>
     *
     * @param oAuth2User the user object returned by the OAuth2 provider
     * @return a new {@link User} entity with OAuth2 profile data.sql
     */
    public User create(@NonNull OAuth2User oAuth2User) {
        String email = UserFieldAdapter.toLower(Objects.requireNonNull(
                oAuth2User.getAttribute(GoogleCredentialsConstants.EMAIL)));

        Role role = getRole(RoleName.ROLE_USER);

        User user = new User(
                email,
                null,
                true, true, true, true,
                Set.of(role),
                AuthProvider.GOOGLE,
                null
        );

        String firstName = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.FIRST_NAME);
        String lastName = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.LAST_NAME);
        String imageUrl = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.PICTURE);
        String oauthId = (String) oAuth2User.getAttributes().get(GoogleCredentialsConstants.OAUTH_ID);

        if (firstName != null && !firstName.isBlank()) {
            log.debug("User first name was set: {}", firstName);
            user.setFirstName(firstName);
        }

        if (lastName != null && !lastName.isBlank()) {
            log.debug("User last name was set: {}", lastName);
            user.setLastName(lastName);
        }

        if (imageUrl != null && !imageUrl.isBlank()) {
            log.debug("User image URL was set: {}", imageUrl);
            user.setImageUrl(imageUrl);
        }

        if (oauthId != null && !oauthId.isBlank()) {
            log.debug("User OAuth ID was set: {}", oauthId);
            user.setOauthId(oauthId);
        }

        return user;
    }

    /**
     * Retrieves a {@link Role} by name.
     *
     * @param roleName the role name (e.g. ROLE_USER)
     * @return the role entity
     * @throws RoleNotFoundException if no such role exists
     */
    private Role getRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format(RoleExceptionMessageProvider.ROLE_NOT_FOUND, roleName)
                ));
    }
}

