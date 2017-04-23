package com.indiepost.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

/**
 * Created by jake on 17. 4. 9.
 */
@Entity
@Table(name = "Visitors")
public class Visitor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 30)
    private String appName;

    @NotNull
    @Size(max = 20)
    private String appVersion;

    private String browser;

    private String browserVersion;

    private String os;

    private String osVersion;

    private String device;

    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Column(name = "userId")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "userAgentId")
    private UserAgent userAgent;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getBrowserVersion() {
        return browserVersion;
    }

    public void setBrowserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }
}
