package com.indiepost.service;

import com.indiepost.dto.stat.*;
import com.indiepost.enums.Types.ActionType;
import com.indiepost.enums.Types.Channel;
import com.indiepost.enums.Types.ClientType;
import com.indiepost.enums.Types.StatType;
import com.indiepost.model.Stat;
import com.indiepost.model.UserAgent;
import com.indiepost.model.Visitor;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.VisitorRepository;
import com.indiepost.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Period;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private final StatRepository statRepository;

    private final VisitorRepository visitorRepository;

    private final PostService postService;

    @Autowired
    public AnalyticsServiceImpl(VisitorRepository visitorRepository, StatRepository statRepository, PostService postService) {
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
        Long visitorId = getVisitorId(request, pageview.getUserId());
        Stat stat = new Stat();
        if (visitorId == null) {
            Visitor visitor = newVisitor(request, pageview.getUserId(), pageview.getAppName(), pageview.getAppVersion());
            visitorId = visitor.getId();
            stat.setLandingPage(true);
            if (StringUtils.isNotEmpty(pageview.getReferrer())) {
                stat.setReferrer(pageview.getReferrer());
            }
            Channel channel = getChannelType(visitor.getBrowser(), pageview.getReferrer());
            stat.setChannel(channel);
        }
        stat.setVisitorId(visitorId);
        String path = pageview.getPath();
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        stat.setPath(path);
        stat.setType(StatType.valueOf(pageview.getType()));
        if (pageview.getPostId() != null) {
            if (!pageview.getAppName().contains("LEGACY")) {
                stat.setPostId(pageview.getPostId());
            } else {
                Long id = postService.findIdByLegacyId(pageview.getPostId());
                stat.setPostId(id);
            }
        }
        stat.setTimestamp(new Date());
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
    public SiteStats getStats(PeriodDto dto) {
        Date since = dto.getSince();
        Date until = dto.getUntil();
        Period period = DateUtils.getPeriod(since, until);

        List<TimeDomainStat> pageviewTrend = statRepository.getPageviewTrend(since, until, period);
        List<TimeDomainStat> visitorTrend = statRepository.getVisitorTrend(since, until, period);

        SiteStats stats = new SiteStats();
        stats.setPeriod(getPeriodString(period));
        stats.setTotalPageview(statRepository.getTotalPageviews(since, until));
        stats.setTotalUniquePageview(statRepository.getTotalUniquePageviews(since, until));
        stats.setTotalPostview(statRepository.getTotalPostviews(since, until));
        stats.setTotalVisitor(statRepository.getTotalVisitors(since, until));
        stats.setTotalAppVisitor(statRepository.getTotalVisitors(since, until, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
        stats.setPageviewTrend(DateUtils.normalizeTimeDomainStats(pageviewTrend, period));
        stats.setVisitorTrend(DateUtils.normalizeTimeDomainStats(visitorTrend, period));
        stats.setTopPagesWebapp(statRepository.getTopPages(since, until, 10L, ClientType.INDIEPOST_WEBAPP));
        stats.setTopPagesMobile(statRepository.getTopPages(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
        stats.setTopPosts(statRepository.getTopPosts(since, until, 10L));
        stats.setTopPostsWebapp(statRepository.getTopPosts(since, until, 10L, ClientType.INDIEPOST_WEBAPP));
        stats.setTopPostsMobile(statRepository.getTopPosts(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
//        stats.setSecondaryPagesWebapp(statRepository.getSecondaryViewedPages(since, until, 10L, ClientType.INDIEPOST_WEBAPP));
//        stats.setSecondaryPagesMobile(statRepository.getSecondaryViewedPages(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
//        stats.setSecondaryPostsWebapp(statRepository.getSecondaryViewedPosts(since, until, 10L, ClientType.INDIEPOST_WEBAPP));
//        stats.setSecondaryPostsMobile(statRepository.getSecondaryViewedPosts(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
//        stats.setTopLandingPagesWebapp(statRepository.getTopLandingPages(since, until, 10L, ClientType.INDIEPOST_WEBAPP));
//        stats.setTopLandingPagesMobile(statRepository.getTopLandingPages(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
//        stats.setTopLandingPostsWebapp(statRepository.getTopLandingPosts(since, until, 10L, ClientType.INDIEPOST_WEBAPP));
//        stats.setTopLandingPostsMobile(statRepository.getTopLandingPosts(since, until, 10L, ClientType.INDIEPOST_LEGACY_MOBILE_APP));
        stats.setPageviewByAuthor(statRepository.getPageviewByAuthor(since, until));
        stats.setPageviewByCategory(statRepository.getPageviewsByCategory(since, until));
        stats.setTopBrowser(statRepository.getTopWebBrowsers(since, until, 10L));
        stats.setTopChannel(statRepository.getTopChannel(since, until, 10L));
        stats.setTopChannel(statRepository.getTopChannel(since, until, 10L));
        stats.setTopReferrer(statRepository.getTopReferrers(since, until, 10L));
        stats.setTopOs(statRepository.getTopOs(since, until, 10L));
        stats.setTopTags(statRepository.getTopTags(since, until, 10L));
        stats.setPageviewPerVisitor(stats.getTotalPageview() / stats.getTotalVisitor().floatValue());
        stats.setPostviewPerVisitor(statRepository.getTotalPageviews(since, until, StatType.POST) / stats.getTotalVisitor().floatValue());

        return stats;
    }

    private String getPeriodString(Period period) {
        if (period.getYears() > 0) {
            return "YEAR";
        } else if (period.getMonths() > 0) {
            return "MONTH";
        } else if (period.getDays() > 0) {
            return "DAY";
        } else {
            return "HOUR";
        }
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

        visitor.setAppName(ClientType.valueOf(appName));
        visitor.setAppVersion(appVersion);
        visitor.setIpAddress(ipAddress);
        visitor.setTimestamp(new Date());

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

    private Channel getChannelType(String browserName, String referrer) {
        if (browserName.toLowerCase().contains("facebook")) {
            return Channel.FACEBOOK;
        }
        if (StringUtils.isEmpty(referrer)) {
            return Channel.NONE;
        }
        String domain = referrer.split("//")[1].split("/")[0];

        if (domain.contains("indiepost")) {
            return Channel.NONE;
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