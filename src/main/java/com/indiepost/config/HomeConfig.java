package com.indiepost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by jake on 17. 2. 26.
 */
@Component
@ConfigurationProperties(prefix = "indiepost.home", locations = "classpath:indiepost.yml")
public class HomeConfig {
    private int fetchCount;

    private String renderingServerUri;

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
}
