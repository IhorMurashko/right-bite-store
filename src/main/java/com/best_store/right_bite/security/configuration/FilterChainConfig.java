package com.best_store.right_bite.security.configuration;

import com.best_store.right_bite.security.accessHandling.RestAccessDeniedHandler;
import com.best_store.right_bite.security.accessHandling.RestAuthEntryPoint;
import com.best_store.right_bite.security.filter.DefaultOncePerRequestFilter;
import com.best_store.right_bite.security.oauht2.GoogleOAuthSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class FilterChainConfig {

    private final RestAccessDeniedHandler accessDeniedHandler;
    private final RestAuthEntryPoint authEntryPoint;
    private final GoogleOAuthSuccessHandler googleOAuthSuccessHandler;
    private final DefaultOncePerRequestFilter defaultOncePerRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/api/*/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/test/**").permitAll()
//                        .anyRequest().authenticated()
//                )
                .oauth2Login(oauth2 ->
                        oauth2
                                .loginPage("/oauth2/authorization/google")
                                .redirectionEndpoint(redirection ->
                                        redirection.baseUri("/login/oauth2/code/*"))
                                .successHandler(googleOAuthSuccessHandler)
                )
                .addFilterBefore(defaultOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*")); // todo: change in prod!
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
