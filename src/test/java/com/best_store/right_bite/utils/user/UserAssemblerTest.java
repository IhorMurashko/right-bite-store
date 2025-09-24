package com.best_store.right_bite.utils.user;

import com.best_store.right_bite.dto.auth.registration.RegistrationCredentialsDto;
import com.best_store.right_bite.exception.exceptions.role.RoleExceptionMP;
import com.best_store.right_bite.exception.exceptions.role.RoleNotFoundException;
import com.best_store.right_bite.model.auth.AuthProvider;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.repository.role.RoleRepository;
import com.best_store.right_bite.security.constant.GoogleCredentialsConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAssemblerTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserAssembler userAssembler;
    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;
    private RegistrationCredentialsDto registrationCredentialsDto;
    private String email;
    private String password;
    private String encodedPassword;
    private Role userRole;
    private RoleName role;
    private String oauthId;
    private Map<String, Object> attributes;
    private OAuth2User oAuth2User;


    @BeforeEach
    void setUp() {
        this.email = "email@email.com";
        this.password = "password";
        this.role = RoleName.ROLE_USER;
        this.encodedPassword = UUID.randomUUID().toString();
        this.registrationCredentialsDto = new RegistrationCredentialsDto(email, password, password);
        this.userRole = new Role(1L, RoleName.ROLE_USER, null);
        this.oauthId = UUID.randomUUID().toString();
        this.attributes = Map.of(
                GoogleCredentialsConstants.EMAIL, email,
                GoogleCredentialsConstants.FIRST_NAME, "firstname",
                GoogleCredentialsConstants.LAST_NAME, "lastname",
                GoogleCredentialsConstants.PICTURE, "picture",
                GoogleCredentialsConstants.OAUTH_ID, oauthId
        );
        this.oAuth2User = new DefaultOAuth2User(null, attributes, "sub");
    }

    @Nested
    class EmailPasswordRegistration {

        @Test
        void shouldThrowRoleNotFoundException_when_roleWasNotFound() {
            doReturn(encodedPassword).when(passwordEncoder).encode(anyString());
            doReturn(Optional.empty()).when(roleRepository).findByName(any(RoleName.class));

            RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () ->
                    userAssembler.create(registrationCredentialsDto));

            assertEquals(String.format(RoleExceptionMP.ROLE_NOT_FOUND, role), exception.getMessage());
            verify(passwordEncoder, times(1)).encode(password);
            verify(roleRepository, times(1)).findByName(any(RoleName.class));
            verifyNoMoreInteractions(passwordEncoder, roleRepository);
        }


        @Test
        void shouldReturnUser_when_registrationCredentialsAreValid() {
            doReturn(encodedPassword).when(passwordEncoder).encode(anyString());
            doReturn(Optional.of(userRole)).when(roleRepository).findByName(any(RoleName.class));

            User user = userAssembler.create(registrationCredentialsDto);

            assertNotNull(user);
            assertEquals(UserFieldAdapter.toLower(email), user.getEmail());
            assertEquals(encodedPassword, user.getPassword());
            verify(passwordEncoder).encode(stringArgumentCaptor.capture());
            assertEquals(password, stringArgumentCaptor.getValue());
            assertEquals(AuthProvider.LOCAL, user.getAuthProvider());
            assertNull(user.getOauthId());
            assertTrue(user.isAccountNonExpired());
            assertTrue(user.isEnabled());
            assertTrue(user.isAccountNonLocked());
            assertTrue(user.isCredentialsNonExpired());
            assertEquals(UserFieldAdapter.toLower(email), user.getEmail());
            assertEquals(1, user.getRoles().size());
            assertTrue(user.getRoles().contains(userRole));
        }

    }

    @Nested
    class OAuth2Authentication {

        @Test
        void shouldThrowRoleNotFoundException_when_roleWasNotFound() {
            doReturn(Optional.empty()).when(roleRepository).findByName(any(RoleName.class));

            RoleNotFoundException exception = assertThrows(RoleNotFoundException.class, () ->
                    userAssembler.create(oAuth2User));

            assertEquals(String.format(RoleExceptionMP.ROLE_NOT_FOUND, role), exception.getMessage());
            verify(roleRepository, times(1)).findByName(any(RoleName.class));
            verifyNoMoreInteractions(roleRepository);
            verifyNoInteractions(passwordEncoder);
        }

        @Test
        void shouldReturnUser_when_googleOAuthCredentialsAreValid() {
            doReturn(Optional.of(userRole)).when(roleRepository).findByName(any(RoleName.class));

            User user = userAssembler.create(oAuth2User);

            assertNotNull(user);
            assertEquals(UserFieldAdapter.toLower(email), user.getEmail());
            assertNull(user.getPassword());
            assertEquals(AuthProvider.GOOGLE, user.getAuthProvider());
            assertEquals(oauthId, user.getOauthId());
            assertTrue(user.isAccountNonExpired());
            assertTrue(user.isEnabled());
            assertTrue(user.isAccountNonLocked());
            assertTrue(user.isCredentialsNonExpired());
            verify(roleRepository, times(1)).findByName(any(RoleName.class));
            verifyNoMoreInteractions(roleRepository);
            verifyNoInteractions(passwordEncoder);
        }
    }
}