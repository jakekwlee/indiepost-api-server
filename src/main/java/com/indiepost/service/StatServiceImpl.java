package com.indiepost.service;

import com.indiepost.dto.Action;
import com.indiepost.dto.Pageview;
import com.indiepost.dto.StatResult;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.ActionType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.Stat;
import com.indiepost.model.UserAgent;
import com.indiepost.model.Visitor;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.VisitorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional
public class StatServiceImpl implements StatService {

    private final StatRepository statRepository;

    private final VisitorRepository visitorRepository;

    private final PostService postService;

    @Autowired
    public StatServiceImpl(VisitorRepository visitorRepository, StatRepository statRepository, PostService postService) {
        this.visitorRepository = visitorRepository;
        this.statRepository = statRepository;
        this.postService = postService;
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id);
    }

    @Override
    public void logPageview(HttpServletRequest request, Pageview pageview) throws IOException {
        if (pageview.getAppName().contains("LEGACY")) {
            logLegacyPageview(request, pageview);
            return;
        }
        Long visitorId = getVisitorId(request, pageview.getUserId());
        Stat stat = new Stat();
        if (visitorId == null) {
            visitorId = newVisitorId(request, pageview.getUserId(), pageview.getAppName(), pageview.getAppVersion());
            if (StringUtils.isNotEmpty(pageview.getReferrer())) {
                stat.setReferrer(pageview.getReferrer());
            }
        }
        stat.setVisitorId(visitorId);
        stat.setPath(pageview.getPath());
        stat.setType(StatType.valueOf(pageview.getType()));
        if (pageview.getPostId() != null) {
            stat.setPostId(pageview.getPostId());
        }
        stat.setTimestamp(new Date());
        statRepository.save(stat);
    }

    @Override
    public void logAction(HttpServletRequest request, Action action) throws IOException {
        Long visitorId = getVisitorId(request, action.getUserId());
        if (visitorId == null) {
            visitorId = newVisitorId(request, action.getUserId(), action.getAppName(), action.getAppVersion());
        }
        Stat stat = new Stat();
        stat.setVisitorId(visitorId);
        stat.setPath(action.getPath());
        stat.setType(StatType.ACTION);
        stat.setAction(ActionType.valueOf(action.getAction()));
        if (StringUtils.isNotEmpty(action.getLabel())) {
            stat.setLabel(action.getLabel());
        }
        if (action.getValue() != null) {
            stat.setValue(action.getValue());
        }
        stat.setTimestamp(new Date());
        statRepository.save(stat);
    }

    @Override
    public List<StatResult> getPageviews(Date since, Date until, Types.Period period) {
        return statRepository.getPageviews(since, until, period);
    }

    @Override
    public List<StatResult> getVisitors(Date since, Date until, Types.Period period) {
        return statRepository.getVisitors(since, until, period);
    }


    private void logLegacyPageview(HttpServletRequest request, Pageview pageview) throws IOException {
        Long visitorId = getVisitorId(request, null);
        Stat stat = new Stat();
        if (visitorId == null) {
            visitorId = newVisitorId(request, null, pageview.getAppName(), pageview.getAppVersion());
            if (StringUtils.isNotEmpty(pageview.getReferrer())) {
                stat.setReferrer(pageview.getReferrer());
            }
        }
        stat.setVisitorId(visitorId);
        stat.setPath(pageview.getPath());
        stat.setType(StatType.valueOf(pageview.getType()));
        if (pageview.getPostId() != null) {
            Long id = postService.findIdByLegacyId(pageview.getPostId());
            stat.setPostId(id);
        }
        stat.setTimestamp(new Date());
        statRepository.save(stat);
    }

    private Long newVisitorId(HttpServletRequest request, Long userId, String appName, String appVersion) throws IOException {
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

        visitor.setAppName(appName);
        visitor.setAppVersion(appVersion);
        visitor.setIpAddress(ipAddress);
        visitor.setTimestamp(new Date());

        visitorRepository.save(visitor);
        HttpSession session = request.getSession();
        session.setAttribute("visitorId", visitor.getId());
        return visitor.getId();
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
