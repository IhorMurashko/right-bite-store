package com.best_store.right_bite.security.oauht2;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.security.constant.GoogleCredentialsConstants;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.managment.TokenManager;
import com.best_store.right_bite.security.propertiesManagement.OAuthRedirectProperties;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.user.UserAssembler;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserCrudService userCrudService;
    private final UserAssembler userAssembler;
    private final TokenManager tokenManager;
    private final OAuthRedirectProperties oAuthRedirectProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.debug("user has been logged in");
        oAuth2User.getAttributes().forEach((k, v) -> log.debug("OAuth attr: {} -> {}", k, v));

        String email = UserFieldAdapter.toLower((String) oAuth2User
                .getAttributes().get(GoogleCredentialsConstants.EMAIL));


        DefaultUserInfoResponseDto userDto;
        if (userCrudService.isEmailExist(email)) {
            userDto = userCrudService.findByEmail(email);
            log.debug("user has been found");
        } else {
            log.debug("user wasn't found");
            userDto = userCrudService.save(userAssembler.create(oAuth2User));
            log.debug("new google user has been saved");
        }

        TokenDto tokenDto = tokenManager.generateDefaultTokens(userDto);

        String redirectUrl = (String) request.getSession().getAttribute(oAuthRedirectProperties.getSessionAttributeName());
        if (redirectUrl == null || redirectUrl.isBlank()) {
            redirectUrl = oAuthRedirectProperties.getDefaultUrl();
        }
        request.getSession().removeAttribute(oAuthRedirectProperties.getSessionAttributeName());

        String targetUrl = String.format("%s#access_token=%s&refresh_token=%s",
                redirectUrl,
                tokenDto.accessToken(),
                tokenDto.refreshToken()
        );

        log.debug("Redirecting to: {}", redirectUrl);

        response.sendRedirect(targetUrl);
    }
}