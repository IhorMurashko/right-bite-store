package com.best_store.right_bite.utils.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds and provides access to JWT token configuration properties.
 * <p>
 * These values are configured via {@code tokens.*} properties in application.yml.
 */
@Component
@ConfigurationProperties(prefix = "tokens")
@Getter
@Setter
public class TokensPropertiesDispatcher {

    /**
     * Access token expiration time in seconds.
     */
    private Long accessTokenAvailableValidityPeriodInSec;

    /**
     * Refresh token expiration time in seconds.
     */
    private Long refreshTokenAvailableValidityPeriodInSec;
}
