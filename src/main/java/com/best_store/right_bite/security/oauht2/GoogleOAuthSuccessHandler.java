package com.best_store.right_bite.security.oauht2;

import com.best_store.right_bite.dto.user.DefaultUserInfoResponseDto;
import com.best_store.right_bite.security.constant.GoogleCredentialsConstants;
import com.best_store.right_bite.security.constant.TokenType;
import com.best_store.right_bite.security.dto.TokenDto;
import com.best_store.right_bite.security.managment.TokenManager;
import com.best_store.right_bite.service.user.crud.UserCrudService;
import com.best_store.right_bite.utils.user.UserAssembler;
import com.best_store.right_bite.utils.user.UserFieldAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

/**
 * Handles successful OAuth2 authentication via Google.
 * <p>
 * If the user already exists in the system (by email), they are loaded.
 * Otherwise, a new user is created and saved using the received OAuth2 data.
 * <p>
 * After successful login, an access and refresh JWT token are generated and returned in JSON format.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserCrudService userCrudService;
    private final UserAssembler userAssembler;
    private final TokenManager tokenManager;

    /**
     * Called after successful OAuth2 authentication.
     *
     * @param request        the HTTP request
     * @param response       the HTTP response (returns token JSON)
     * @param authentication an authenticated OAuth2 user
     * @throws IOException if writing to response fails
     */
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request,
//                                        HttpServletResponse response,
//                                        Authentication authentication) throws IOException {
//
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        log.debug("Google login success. Extracting user data...");
//
//        String email = (String) oAuth2User.getAttributes().get("email");
//        DefaultUserInfoResponseDto userDto = userCrudService.isEmailExist(email)
//                ? userCrudService.findByEmail(email)
//                : userCrudService.save(userAssembler.create(oAuth2User));
//
//        TokenDto tokenDto = tokenManager.generateDefaultTokens(userDto);
//
//        // Создаём безопасные cookies
//        ResponseCookie accessCookie = ResponseCookie.from("accessToken", tokenDto.accessToken())
//                .httpOnly(true)      // JS не имеет доступа к cookie
//                .secure(true)        // только по HTTPS
//                .sameSite("None")    // нужно для кросс-доменных запросов
//                .path("/")
//                .maxAge(Duration.ofHours(1))
//                .build();
//
//        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokenDto.refreshToken())
//                .httpOnly(true)
//                .secure(true)
//                .sameSite("None")
//                .path("/")
//                .maxAge(Duration.ofDays(7))
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
//        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
//
//        // Восстанавливаем redirect URL из параметра state
//        String redirectUri = request.getParameter("state");
//        if (redirectUri == null || redirectUri.isBlank()) {
//            redirectUri = "https://the-right-bit-frontend-shop.vercel.app"; // fallback
//        }
//
//        log.debug("Redirecting user to {}", redirectUri);
//        response.sendRedirect(redirectUri);
//    }
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

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        String json = new ObjectMapper().writeValueAsString(Map.of(
                TokenType.ACCESS, tokenDto.accessToken(),
                TokenType.REFRESH, tokenDto.refreshToken()
        ));
        response.getWriter().write(json);
    }
}