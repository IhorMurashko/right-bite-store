package com.best_store.right_bite.util.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "tokens")
@Getter
@Setter
public class TokensPropertiesDispatcher {
    private Long accessTokenAvailableValidityPeriodInSec;
    private Long refreshTokenAvailableValidityPeriodInSec;
}
