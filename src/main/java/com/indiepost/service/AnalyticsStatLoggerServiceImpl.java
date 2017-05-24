package com.indiepost.service;

import com.indiepost.dto.stat.Action;
import com.indiepost.dto.stat.Pageview;
import com.indiepost.enums.Types;
import com.indiepost.model.Stat;
import com.indiepost.model.UserAgent;
import com.indiepost.model.Visitor;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.VisitorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 5. 25.
 */
@Service
@Transactional
public class AnalyticsStatLoggerServiceImpl implements AnalyticsStatLoggerService {

    private final VisitorRepository visitorRepository;

    private final StatRepository statRepository;

    private final PostRepository postRepository;

    @Autowired
    public AnalyticsStatLoggerServiceImpl(VisitorRepository visitorRepository, StatRepository statRepository, PostRepository postRepository) {
        this.visitorRepository = visitorRepository;
        this.statRepository = statRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id);
    }

    @Override
    public void logPageview(HttpServletRequest request, Pageview pageview) throws IOException {
        Long visitorId = getVisitorId(request, pageview.getUserId());
        Stat stat = new Stat();
        if (visitorId == null) {
            Visitor visitor = newVisitor(request, pageview.getUserId(), pageview.getAppName(), pageview.getAppVersion());
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
    public void logAction(HttpServletRequest request, Action action) throws IOException {
        Long visitorId = getVisitorId(request, action.getUserId());
        if (visitorId == null) {
            Visitor visitor = newVisitor(request, action.getUserId(), action.getAppName(), action.getAppVersion());
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


    private Visitor newVisitor(HttpServletRequest request, Long userId, String appName, String appVersion) throws IOException {
        String userAgentString = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();

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
        HttpSession session = request.getSession();
        session.setAttribute("visitorId", visitor.getId());
        return visitor;
    }

    private Long getVisitorId(HttpServletRequest request, Long userId) throws IOException {
        HttpSession session = request.getSession();
        Long visitorId = (Long) session.getAttribute("visitorId");

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
