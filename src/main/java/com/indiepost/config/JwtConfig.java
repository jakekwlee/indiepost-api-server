package com.indiepost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by jake on 7/22/17.
 */
@Configuration
@PropertySource("classpath:webapp-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp.jwt")
public class JwtConfig {
    private String tokenPrefix;

    private String httpHeaderName;

    private long tokenExpiration;

    private long rememberMeExpiration;

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public String getHttpHeaderName() {
        return httpHeaderName;
    }

    public void setHttpHeaderName(String httpHeaderName) {
        this.httpHeaderName = httpHeaderName;
    }

    public long getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public long getRememberMeExpiration() {
        return rememberMeExpiration;
    }

    public void setRememberMeExpiration(long rememberMeExpiration) {
        this.rememberMeExpiration = rememberMeExpiration;
    }
}
