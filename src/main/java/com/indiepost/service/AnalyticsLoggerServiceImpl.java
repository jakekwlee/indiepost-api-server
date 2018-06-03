package com.indiepost.service;

import com.indiepost.dto.stat.ActionDto;
import com.indiepost.dto.stat.PageviewDto;
import com.indiepost.enums.Types.Channel;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.model.UserAgent;
import com.indiepost.model.analytics.*;
import com.indiepost.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by jake on 17. 5. 25.
 */
@Service
@Transactional
public class AnalyticsLoggerServiceImpl implements AnalyticsLoggerService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsLoggerServiceImpl.class);

    private final VisitorRepository visitorRepository;

    private final PageviewRepository pageviewRepository;

    private final ActionRepository actionRepository;

    private final LinkRepository linkRepository;

    private final ClickRepository clickRepository;

    private final UserService userService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Autowired
    public AnalyticsLoggerServiceImpl(VisitorRepository visitorRepository, PageviewRepository pageviewRepository,
                                      ActionRepository actionRepository,
                                      UserService userService, LinkRepository linkRepository, ClickRepository clickRepository) {
        this.visitorRepository = visitorRepository;
        this.pageviewRepository = pageviewRepository;
        this.actionRepository = actionRepository;
        this.userService = userService;
        this.linkRepository = linkRepository;
        this.clickRepository = clickRepository;
    }

    @Override
    public Visitor findOne(Long id) {
        return visitorRepository.findOne(id);
    }

    @Override
    public void logPageview(HttpServletRequest req, HttpServletResponse res, PageviewDto pageviewDto) throws IOException {
        Long visitorId = getVisitorId(req, pageviewDto.getUserId());
        Pageview pageview = new Pageview();

        if (visitorId == null) {
            Visitor visitor = newVisitor(req, res, pageviewDto.getUserId(), pageviewDto.getAppName(), pageviewDto.getAppVersion());
            if (visitor == null) {
                return;
            }
            visitorId = visitor.getId();
            pageview.setLandingPage(true);

            if (isNotEmpty(pageviewDto.getReferrer())) {
                String referrer = pageviewDto.getReferrer();
                if (referrer.length() > 1 && referrer.endsWith("/")) {
                    referrer = referrer.substring(0, referrer.length() - 1);
                }
                visitor.setReferrer(referrer);
            }
            Channel channel = getChannelType(visitor.getBrowser(), pageviewDto.getReferrer());
            visitor.setChannel(channel);
        }
        pageview.setVisitorId(visitorId);
        pageview.setPath(pageviewDto.getPath());
        if (pageviewDto.getPostId() != null) {
            pageview.setPostId(pageviewDto.getPostId());
        }
        pageview.setTimestamp(LocalDateTime.now());
        pageviewRepository.save(pageview);
    }

    @Override
    public void logAction(HttpServletRequest req, HttpServletResponse res, ActionDto actionDto) throws IOException {
        Long visitorId = getVisitorId(req, actionDto.getUserId());
        if (visitorId == null) {
            Visitor visitor = newVisitor(req, res, actionDto.getUserId(), actionDto.getAppName(), actionDto.getAppVersion());
            if (visitor == null) {
                return;
            }
            visitorId = visitor.getId();
        }
        Action action = new Action();
        action.setVisitorId(visitorId);
        action.setPath(actionDto.getPath());
        action.setActionType(actionDto.getActionType());
        if (isNotEmpty(actionDto.getLabel())) {
            action.setLabel(actionDto.getLabel());
        }
        if (actionDto.getValue() != null) {
            action.setValue(actionDto.getValue());
        }
        action.setTimestamp(LocalDateTime.now());
        actionRepository.save(action);
    }

    @Override
    public String logClickAndGetLink(HttpServletRequest req, HttpServletResponse res, Principal principal, String linkUid) throws IOException {
        if (isEmpty(linkUid)) {
            return "/";
        }
        Link link = linkRepository.findByUid(linkUid);
        if (link == null) {
            return "/";
        }
        Long userId = null;
        if (principal != null) {
            userId = userService.findCurrentUser().getId();
        }
        Long visitorId = getVisitorId(req, userId);
        if (visitorId == null) {
            Visitor visitor = newVisitor(req, res, userId, ClientType.INDIEPOST_AD_ENGINE.toString(), "0.9.0");
            if (visitor == null) {
                return link.getUrl();
            }
            visitorId = visitor.getId();
        }
        Click click = new Click();
        click.setVisitorId(visitorId);
        click.setPath(req.getRequestURI());
        click.setLink(link);
        click.setTimestamp(LocalDateTime.now());
        clickRepository.save(click);
        return link.getUrl();
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

        if (isNotEmpty(deviceName)) {
            if (deviceName.contains("Spider")) {
                logger.info("A visitor is filtered by blacklist, skip DB insert: {} : {} : {}",
                        browserName, ipAddress, req.getRequestURI());
                return null;
            }
            visitor.setBrowser(browserName);
            visitor.setBrowserVersion(getBrowserVersion(ua.userAgent));
        } else {
            UserAgent userAgent = new UserAgent();
            userAgent.setUaString(userAgentString);
            userAgent.setUaHash(userAgentString);
            visitor.setUserAgent(userAgent);
        }

        if (isNotEmpty(osName)) {
            visitor.setOs(osName);
            visitor.setOsVersion(this.getOsVersion(ua.os));
        }

        if (isNotEmpty(deviceName)) {
            visitor.setDevice(deviceName);
        }

        if (userId != null) {
            visitor.setUserId(userId);
        }

        if (ClientType.INDIEPOST_AD_ENGINE.toString().equals(appName)) {
            visitor.setAdVisitor(true);
        }
        visitor.setAppName(appName);
        visitor.setAppVersion(appVersion);
        visitor.setIpAddress(ipAddress);
        visitor.setTimestamp(LocalDateTime.now());

        Long visitorId = visitorRepository.save(visitor);

        Cookie cookie = new Cookie("visitorId", visitorId.toString());
        cookie.setMaxAge(1800); // expires after 30min

        if (activeProfile.equals("prod")) {
            String domainName = req.getServerName();
            String domainNamePrefix = domainName.substring(domainName.indexOf("."), domainName.length());
            cookie.setDomain(domainNamePrefix);
        }
        res.addCookie(cookie);
        return visitor;
    }

    private Long getVisitorId(HttpServletRequest request, Long userId) {
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
            Visitor visitor = this.findOne(visitorId);
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

    private Channel getChannelType(String browserName, String referrer) {
        if (browserName.toLowerCase().contains("facebook")) {
            return Channel.FACEBOOK;
        }
        if (isEmpty(referrer)) {
            return Channel.NONE;
        }
        String domain = referrer.split("//")[1].split("/")[0];

        if (domain.contains("indiepost.co.kr")) {
            return Channel.INBOUND_LINK;
        }
        if (domain.contains("goo")) {
            return Channel.GOOGLE;
        }
        if (domain.contains("facebook") || domain.contains("fb")) {
            return Channel.FACEBOOK;
        }
        if (domain.contains("t.co")) {
            return Channel.TWITTER;
        }
        if (domain.contains("naver")) {
            return Channel.NAVER;
        }
        if (domain.contains("daum")) {
            return Channel.DAUM;
        }
        if (domain.contains("instagram")) {
            return Channel.INSTAGRAM;
        }

        return Channel.OTHER;
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
