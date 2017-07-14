package com.indiepost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by jake on 17. 2. 26.
 */
@Configuration
@PropertySource("classpath:webapp-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp")
public class HomeConfig {
    private boolean serverSideRendering;
    private int fetchCount;
    private String renderingServerUri;
    private String baseUrl;
    private String resourcesPath;

    public boolean isServerSideRendering() {
        return serverSideRendering;
    }

    public void setServerSideRendering(boolean serverSideRendering) {
        this.serverSideRendering = serverSideRendering;
    }

    public String getRenderingServerUri() {
        return renderingServerUri;
    }

    public void setRenderingServerUri(String renderingServerUri) {
        this.renderingServerUri = renderingServerUri;
    }

    public int getFetchCount() {
        return fetchCount;
    }

    public void setFetchCount(int fetchCount) {
        this.fetchCount = fetchCount;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getResourcesPath() {
        return resourcesPath;
    }

    public void setResourcesPath(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }
}
