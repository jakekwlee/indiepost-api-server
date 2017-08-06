package com.indiepost.service;

import com.indiepost.dto.stat.Action;
import com.indiepost.dto.stat.Pageview;
import com.indiepost.enums.Types;
import com.indiepost.model.UserAgent;
import com.indiepost.model.analytics.Stat;
import com.indiepost.model.analytics.Visitor;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.VisitorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 5. 25.
 */
@Service
@Transactional
public class AnalyticsLoggerServiceImpl implements AnalyticsLoggerService {

    private final VisitorRepository visitorRepository;

    private final StatRepository statRepository;

    private final PostRepository postRepository;

    @Autowired
    public AnalyticsLoggerServiceImpl(VisitorRepository visitorRepository, StatRepository statRepository, PostRepository postRepository) {
        this.visitorRepository = visitorRepository;
        this.statRepository = statRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id);
    }

    @Override
    public void logPageview(HttpServletRequest req, HttpServletResponse res, Pageview pageview) throws IOException {
        Long visitorId = getVisitorId(req, pageview.getUserId());
        Stat stat = new Stat();
        if (visitorId == null) {
            Visitor visitor = newVisitor(req, res, pageview.getUserId(), pageview.getAppName(), pageview.getAppVersion());
            visitorId = visitor.getId();
            stat.setLandingPage(true);
            if (StringUtils.isNotEmpty(pageview.getReferrer())) {
                String referrer = pageview.getReferrer();
                if (referrer.length() > 1 && referrer.endsWith("/")) {
                    referrer = referrer.substring(0, referrer.length() - 1);
                }
                stat.setReferrer(referrer);
            }
            Types.Channel channel = getChannelType(visitor.getBrowser(), pageview.getReferrer());
            stat.setChannel(channel);
        }
        stat.setVisitorId(visitorId);
        stat.setPath(pageview.getPath());
        stat.setType(Types.StatType.valueOf(pageview.getType()));
        if (pageview.getPostId() != null) {
            if (!pageview.getAppName().contains("LEGACY")) {
                stat.setPostId(pageview.getPostId());
            } else {
                Long id = postRepository.findIdByLegacyId(pageview.getPostId());
                stat.setPostId(id);
            }
        }
        stat.setTimestamp(LocalDateTime.now());
        statRepository.save(stat);
    }

    @Override
    public void logAction(HttpServletRequest req, HttpServletResponse res, Action action) throws IOException {
        Long visitorId = getVisitorId(req, action.getUserId());
        if (visitorId == null) {
            Visitor visitor = newVisitor(req, res, action.getUserId(), action.getAppName(), action.getAppVersion());
            visitorId = visitor.getId();
        }
        Stat stat = new Stat();
        stat.setVisitorId(visitorId);
        stat.setPath(action.getPath());
        stat.setType(Types.StatType.ACTION);
        stat.setAction(Types.ActionType.valueOf(action.getAction()));
        if (StringUtils.isNotEmpty(action.getLabel())) {
            stat.setLabel(action.getLabel());
        }
        if (action.getValue() != null) {
            stat.setValue(action.getValue());
        }
        stat.setTimestamp(LocalDateTime.now());
        statRepository.save(stat);
    }

    @Override
    public String logAndGetLink(String uid) {
        return null;
    }


    private Visitor newVisitor(HttpServletRequest req, HttpServletResponse res, Long userId, String appName, String appVersion) throws IOException {
        String userAgentString = req.getHeader("User-Agent");
        String ipAddress = getIpAddress(req);

        Visitor visitor = new Visitor();
        ua_parser.Parser parser = new ua_parser.Parser();
        ua_parser.Client ua = parser.parse(userAgentString);

        String browserName = ua.userAgent.family;
        String osName = ua.os.family;
        String deviceName = ua.device.family;

        if (StringUtils.isNotEmpty(browserName)) {
            visitor.setBrowser(browserName);
            visitor.setBrowserVersion(getBrowserVersion(ua.userAgent));
        } else {
            UserAgent userAgent = new UserAgent();
            userAgent.setUaString(userAgentString);
            userAgent.setUaHash(userAgentString);
            visitor.setUserAgent(userAgent);
        }

        if (StringUtils.isNotEmpty(osName)) {
            visitor.setOs(osName);
            visitor.setOsVersion(this.getOsVersion(ua.os));
        }

        if (StringUtils.isNotEmpty(deviceName)) {
            visitor.setDevice(deviceName);
        }

        if (userId != null) {
            visitor.setUserId(userId);
        }

        visitor.setAppName(Types.ClientType.valueOf(appName));
        visitor.setAppVersion(appVersion);
        visitor.setIpAddress(ipAddress);
        visitor.setTimestamp(LocalDateTime.now());

        visitorRepository.save(visitor);

        Cookie cookie = new Cookie("visitorId", visitor.getId().toString());
        cookie.setMaxAge(1800); // expires after 30min
        res.addCookie(cookie);
        return visitor;
    }

    private Long getVisitorId(HttpServletRequest request, Long userId) throws IOException {
        Cookie[] cookieArray = request.getCookies();
        if (cookieArray == null) {
            return null;
        }

        Long visitorId = null;
        for (Cookie cookie : cookieArray) {
            if (cookie.getName().equals("visitorId")) {
                visitorId = Long.parseLong(cookie.getValue());
                break;
            }
        }

        if (visitorId == null) {
            return null;
        }

        if (userId != null) {
            Visitor visitor = this.findVisitorById(visitorId);
            if (visitor.getUserId() == null) {
                visitor.setUserId(userId);
                visitorRepository.save(visitor);
            }
        }
        return visitorId;
    }

    private String getIpAddress(HttpServletRequest req) {
        String ip = req.getHeader("X-FORWARDED-FOR");
        return ip != null ? ip : req.getRemoteAddr();
    }

    private Types.Channel getChannelType(String browserName, String referrer) {
        if (browserName.toLowerCase().contains("facebook")) {
            return Types.Channel.FACEBOOK;
        }
        if (StringUtils.isEmpty(referrer)) {
            return Types.Channel.NONE;
        }
        String domain = referrer.split("//")[1].split("/")[0];

        if (domain.contains("indiepost")) {
            return Types.Channel.NONE;
        }
        if (domain.contains("goo")) {
            return Types.Channel.GOOGLE;
        }
        if (domain.contains("facebook") || domain.contains("fb")) {
            return Types.Channel.FACEBOOK;
        }
        if (domain.contains("t.co")) {
            return Types.Channel.TWITTER;
        }
        if (domain.contains("naver")) {
            return Types.Channel.NAVER;
        }
        if (domain.contains("daum")) {
            return Types.Channel.DAUM;
        }
        if (domain.contains("instagram")) {
            return Types.Channel.INSTAGRAM;
        }

        return Types.Channel.OTHER;
    }

    private String getBrowserVersion(ua_parser.UserAgent userAgent) {
        StringBuilder stringBuilder = new StringBuilder();
        if (userAgent.major != null) {
            stringBuilder.append(userAgent.major);
            if (userAgent.minor != null) {
                stringBuilder.append('.');
                stringBuilder.append(userAgent.minor);
                if (userAgent.patch != null) {
                    stringBuilder.append('.');
                    stringBuilder.append(userAgent.patch);
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

    private String getOsVersion(ua_parser.OS os) {
        StringBuilder sb = new StringBuilder();
        if (os.major != null) {
            sb.append(os.major);
            if (os.minor != null) {
                sb.append('.');
                sb.append(os.minor);
                if (os.patch != null) {
                    sb.append('.');
                    sb.append(os.patch);
                    if (os.patchMinor != null) {
                        sb.append('.');
                        sb.append(os.patchMinor);
                    }
                }
            }
            return sb.toString();
        }
        return null;
    }
}
