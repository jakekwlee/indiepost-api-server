package com.indiepost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

/**
 * Created by jake on 17. 2. 26.
 */
@Configuration
@PropertySource("classpath:webapp-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "webapp")
public class AppConfig {

    private String baseUrl;

    private String renderingServerUrl;

    private boolean serverSideRendering;

    private String cdnUrl;

    private String staticRootPath;

    private String imageUploadPath;

    private String mediaUploadPath;

    private int imageFilenameLength;

    private String imageFilenameFormat;

    private List<String> acceptedImageTypes;

    private int fetchCount;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getRenderingServerUrl() {
        return renderingServerUrl;
    }

    public void setRenderingServerUrl(String renderingServerUrl) {
        this.renderingServerUrl = renderingServerUrl;
    }

    public boolean isServerSideRendering() {
        return serverSideRendering;
    }

    public void setServerSideRendering(boolean serverSideRendering) {
        this.serverSideRendering = serverSideRendering;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    public String getStaticRootPath() {
        return staticRootPath;
    }

    public void setStaticRootPath(String staticRootPath) {
        this.staticRootPath = staticRootPath;
    }

    public String getImageUploadPath() {
        return imageUploadPath;
    }

    public void setImageUploadPath(String imageUploadPath) {
        this.imageUploadPath = imageUploadPath;
    }

    public String getMediaUploadPath() {
        return mediaUploadPath;
    }

    public void setMediaUploadPath(String mediaUploadPath) {
        this.mediaUploadPath = mediaUploadPath;
    }

    public int getImageFilenameLength() {
        return imageFilenameLength;
    }

    public void setImageFilenameLength(int imageFilenameLength) {
        this.imageFilenameLength = imageFilenameLength;
    }

    public String getImageFilenameFormat() {
        return imageFilenameFormat;
    }

    public void setImageFilenameFormat(String imageFilenameFormat) {
        this.imageFilenameFormat = imageFilenameFormat;
    }

    public List<String> getAcceptedImageTypes() {
        return acceptedImageTypes;
    }

    public void setAcceptedImageTypes(List<String> acceptedImageTypes) {
        this.acceptedImageTypes = acceptedImageTypes;
    }

    public int getFetchCount() {
        return fetchCount;
    }

    public void setFetchCount(int fetchCount) {
        this.fetchCount = fetchCount;
    }
}
