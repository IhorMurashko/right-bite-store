package com.best_store.right_bite.security.propertiesManagement;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Getter
public class OAuthRedirectProperties {

    @Value("${auth.redirect.default-url}")
    private String defaultUrl;
    @Value("${auth.redirect.session-attribute-name}")
    private String sessionAttributeName;
}

