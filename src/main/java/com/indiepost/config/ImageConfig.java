package com.indiepost.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Created by jake on 11/5/16.
 */

@ConfigurationProperties(prefix = "indiepost.images")
public class ImageConfig {
    private String fsRoot;

    private String fsLocation;

    private String dbLocation;

    private String apiUri;

    private int filenameLength;

    private String filenameFormat;

    private List<String> acceptedTypes;

    public List<String> getAcceptedTypes() {
        return acceptedTypes;
    }

    public void setAcceptedTypes(List<String> acceptedTypes) {
        this.acceptedTypes = acceptedTypes;
    }

    public String getFsRoot() {
        return fsRoot;
    }

    public void setFsRoot(String fsRoot) {
        this.fsRoot = fsRoot;
    }

    public String getFsLocation() {
        return fsLocation;
    }

    public void setFsLocation(String fsLocation) {
        this.fsLocation = fsLocation;
    }

    public String getDbLocation() {
        return dbLocation;
    }

    public void setDbLocation(String dbLocation) {
        this.dbLocation = dbLocation;
    }

    public String getApiUri() {
        return apiUri;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public int getFilenameLength() {
        return filenameLength;
    }

    public void setFilenameLength(int filenameLength) {
        this.filenameLength = filenameLength;
    }

    public String getFilenameFormat() {
        return filenameFormat;
    }

    public void setFilenameFormat(String filenameFormat) {
        this.filenameFormat = filenameFormat;
    }


}
