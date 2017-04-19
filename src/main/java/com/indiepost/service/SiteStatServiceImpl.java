package com.indiepost.service;

import com.indiepost.dto.PageviewDto;
import com.indiepost.enums.Types.Client;
import com.indiepost.enums.Types.ContentType;
import com.indiepost.model.Pageview;
import com.indiepost.model.UserAgent;
import com.indiepost.model.Visitor;
import com.indiepost.repository.PageviewRepository;
import com.indiepost.repository.VisitorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
@Transactional
public class SiteStatServiceImpl implements SiteStatService {

    private final PageviewRepository pageviewRepository;

    private final VisitorRepository visitorRepository;

    @Autowired
    public SiteStatServiceImpl(VisitorRepository visitorRepository, PageviewRepository pageviewRepository) {
        this.visitorRepository = visitorRepository;
        this.pageviewRepository = pageviewRepository;
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id);
    }

    @Override
    public void log(HttpServletRequest request, PageviewDto dto) throws IOException {
        HttpSession session = request.getSession();
        Long visitorId = (Long) session.getAttribute("visitorId");
        Visitor visitor;
        if (visitorId == null) {
            String userAgentString = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            visitor = newVisitor(dto, userAgentString, ipAddress);
            session.setAttribute("visitorId", visitor.getId());
        } else {
            if (dto.getUserId() != null) {
                visitor = this.findVisitorById(visitorId);
                if (visitor.getUserId() == null) {
                    visitor.setUserId(dto.getUserId());
                    visitorRepository.save(visitor);
                }
            }
        }
        logPageview(dto, visitorId);
    }

    private Visitor newVisitor(PageviewDto dto, String userAgentString, String ipAddress) throws IOException {
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

        if (dto.getUserId() != null) {
            visitor.setUserId(dto.getUserId());
        }

        Client client = Client.valueOf(dto.getClient());
        visitor.setClient(client);
        visitor.setIpAddress(ipAddress);
        visitor.setTimestamp(new Date());

        visitorRepository.save(visitor);
        return visitor;
    }

    private void logPageview(PageviewDto dto, Long visitorId) {
        Pageview pageview = new Pageview();
        pageview.setVisitorId(visitorId);
        pageview.setPath(dto.getPath());
        ContentType contentType = ContentType.valueOf(dto.getContentType());
        pageview.setContentType(contentType);
        if (StringUtils.isNotEmpty(dto.getReferrer())) {
            pageview.setReferrer(dto.getReferrer());
        }
        if (dto.getPostId() != null) {
            pageview.setPostId(dto.getPostId());
        }
        pageview.setTimestamp(new Date());
        pageviewRepository.save(pageview);
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
