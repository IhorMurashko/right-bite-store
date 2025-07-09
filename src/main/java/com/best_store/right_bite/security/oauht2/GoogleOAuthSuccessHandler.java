package com.best_store.right_bite.security.oauht2;

import com.best_store.right_bite.model.auth.AuthProvider;
import com.best_store.right_bite.model.role.Role;
import com.best_store.right_bite.model.role.RoleName;
import com.best_store.right_bite.model.user.User;
import com.best_store.right_bite.security.constant.GoogleCredentialsConstants;
import com.best_store.right_bite.security.constant.TokenClaimsConstants;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.managment.TokenManager;
import com.best_store.right_bite.service.user.UserCrudService;
import com.best_store.right_bite.util.UserFieldAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenManager tokenManager;
    private final UserCrudService userCrudService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.debug("user has been logged in");
        oAuth2User.getAttributes().forEach((k, v) -> log.debug("OAuth attr: {} -> {}", k, v));

        String email = UserFieldAdapter.toLower((String) oAuth2User
                .getAttributes().get(GoogleCredentialsConstants.EMAIL));


        User user;

        //todo: separate user create logic
        if (userCrudService.isEmailExist(email)) {
            user = userCrudService.findByEmail(email);
            log.debug("user has been found");
        } else {
            log.debug("user wasn't found");
            //todo:
            Set<Role> defaultRoles = Set.of(new Role(RoleName.ROLE_USER));
            user = new User(
                    email, null,
                    true, true
                    , true, true,
                    defaultRoles, AuthProvider.GOOGLE, null);

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

            User saved = userCrudService.save(user);
            log.debug("new google user has been saved");
            user = saved;
        }

        Map<String, Object> claims = Map.of(
                TokenClaimsConstants.USERNAME_CLAIM, user.getEmail(),
                //todo: utils role parser
                TokenClaimsConstants.ROLES_CLAIM, user.getRoles().stream()
                        .map(role -> role.getName().name())
                        .collect(Collectors.toList())
        );

        TokenDto tokenDto = tokenManager
                .generateToken(String.valueOf(user.getId()), claims);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getOutputStream(), tokenDto);

    }
}
