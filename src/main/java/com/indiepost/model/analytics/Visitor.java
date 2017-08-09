package com.indiepost.model.analytics;

import com.indiepost.enums.Types;
import com.indiepost.model.User;
import com.indiepost.model.UserAgent;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 9.
 */
@Entity
@Table(name = "Visitors", indexes = {
        @Index(columnList = "timestamp", name = "v_timestamp_idx")
})
@NamedNativeQueries({
        @NamedNativeQuery(name = "@GET_VISITORS_TREND_HOURLY",
                query = "SELECT date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY date(v.timestamp), hour(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_VISITORS_TREND_DAILY",
                query = "SELECT date(v.timestamp) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY date(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_VISITORS_TREND_MONTHLY",
                query = "SELECT date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY year(v.timestamp), month(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_VISITORS_TREND_YEARLY",
                query = "SELECT makedate(year(v.timestamp), 1) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY year(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_TOP_REFERRERS",
                query = "SELECT v.referrer AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "AND v.referrer IS NOT NULL " +
                        "GROUP BY v.referrer " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_REFERRERS_BY_CLIENT",
                query = "SELECT v.referrer AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.referrer IS NOT NULL " +
                        "AND v.appName = :client " +
                        "GROUP BY v.referrer " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_WEB_BROWSERS",
                query = "SELECT v.browser AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "AND v.referrer IS NOT NULL " +
                        "GROUP BY v.browser " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_WEB_BROWSERS_BY_CLIENT",
                query = "SELECT v.browser AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.referrer IS NOT NULL " +
                        "AND v.appName = :client " +
                        "GROUP BY v.browser " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_OS",
                query = "SELECT v.os AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY v.os " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_OS_BY_CLIENT",
                query = "SELECT v.os AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.appName = :client " +
                        "GROUP BY v.os " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_CHANNELS",
                query = "SELECT v.channel AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY v.channel " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_CHANNELS_BY_CLIENT",
                query = "SELECT v.channel AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.appName = :client " +
                        "GROUP BY v.channel " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit")
})
public class Visitor {

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
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "userAgentId")
    private UserAgent userAgent;

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

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(UserAgent userAgent) {
        this.userAgent = userAgent;
    }
}
