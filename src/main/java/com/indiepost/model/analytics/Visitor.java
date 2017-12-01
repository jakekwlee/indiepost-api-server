package com.indiepost.model.analytics;

import com.indiepost.enums.Types;
import com.indiepost.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 9.
 */
@Entity
@Table(name = "Visitors", indexes = {
        @Index(columnList = "timestamp", name = "v_timestamp_idx")
})
public class Visitor implements Serializable {

    private static final long serialVersionUID = -7650167348298591944L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 40)
    private String appName;

    @Size(max = 20)
    private String appVersion;

    @Size(max = 40)
    private String browser;

    @Size(max = 20)
    private String browserVersion;

    @Size(max = 40)
    private String os;

    @Size(max = 20)
    private String osVersion;

    @Size(max = 40)
    private String device;

    @Size(max = 40)
    private String ipAddress;

    @Size(max = 500)
    private String referrer;

    private boolean adVisitor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Types.Channel channel = Types.Channel.NONE;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @Column(name = "userId")
    private Long userId;

    public boolean isAdVisitor() {
        return adVisitor;
    }

    public void setAdVisitor(boolean adVisitor) {
        this.adVisitor = adVisitor;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public Types.Channel getChannel() {
        return channel;
    }

    public void setChannel(Types.Channel channel) {
        this.channel = channel;
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

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

}
