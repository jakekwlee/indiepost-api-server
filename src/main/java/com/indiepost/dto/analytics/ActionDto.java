package com.indiepost.dto.analytics;

/**
 * Created by jake on 17. 4. 21.
 */
public class ActionDto {
    private String appName;

    private String appVersion;

    private String path;

    private String actionType;

    private String label;

    private int value;

    private Long userId;

    private ActionDto(Builder builder) {
        this.appName = builder.appName;
        this.appVersion = builder.appVersion;
        this.path = builder.path;
        this.actionType = builder.actionType;
        this.label = builder.label;
        this.value = builder.value;
        this.userId = builder.userId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static class Builder {
        private String appName;

        private String appVersion;

        private String path;

        private String actionType;

        private String label = null;

        private int value = 0;

        private Long userId;

        public Builder(String appName, String appVersion, String path, String actionType) {
            this.appName = appName;
            this.appVersion = appVersion;
            this.path = path;
            this.actionType = actionType;
        }

        public ActionDto build() {
            return new ActionDto(this);
        }

        public Builder label(String label) {
            if (label != null) {
                this.label = label;
            }
            return this;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }

        public Builder userId(Long userId) {
            if (userId != null) {
                this.userId = userId;
            }
            return this;
        }
    }
}
