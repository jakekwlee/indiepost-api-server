package com.indiepost.dto.stat;

import javax.validation.constraints.NotNull;

/**
 * Created by jake on 17. 4. 21.
 */
public class ActionDto {

    private Long id;

    @NotNull
    private String appName;

    @NotNull
    private String appVersion;

    @NotNull
    private String path;

    @NotNull
    private String actionType;

    private String label;

    private Integer value;

    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setActionType(String type) {
        this.actionType = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
