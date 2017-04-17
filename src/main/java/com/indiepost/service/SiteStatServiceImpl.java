package com.indiepost.service;

import com.indiepost.enums.Types;
import com.indiepost.model.Pageview;
import com.indiepost.model.User;
import com.indiepost.model.Visitor;
import com.indiepost.repository.PageviewRepository;
import com.indiepost.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.OS;
import ua_parser.Parser;
import ua_parser.UserAgent;

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

    private final UserService userService;

    @Autowired
    public SiteStatServiceImpl(VisitorRepository visitorRepository, PageviewRepository pageviewRepository, UserService userService) {
        this.visitorRepository = visitorRepository;
        this.pageviewRepository = pageviewRepository;
        this.userService = userService;
    }

    @Override
    public void log(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();
        Long visitorId = (Long) session.getAttribute("visitorId");
        if (session.isNew() || visitorId == null) {
            Visitor visitor = new Visitor();
            String userAgentString = request.getHeader("User-Agent");
            Parser parser = new Parser();
            Client client = parser.parse(userAgentString);
            String device = client.device.family;
            if (device == null) {
                com.indiepost.model.UserAgent userAgent = new com.indiepost.model.UserAgent();
                userAgent.setUaString(userAgentString);
                userAgent.setUaHash(userAgentString);
                visitor.setUserAgent(userAgent);
                visitor.setClient(Types.Client.Unknown);
            } else {
                visitor.setBrowser(client.userAgent.family);
                visitor.setBrowserVersion(getBrowserVersion(client.userAgent));
                visitor.setOs(client.os.family);
                visitor.setOsVersion(getOsVersion(client.os));
                visitor.setDevice(device);
                User user = userService.getCurrentUser();
                if (user != null) {
                    visitor.setUser(user);
                }
            }
            String ipAddress = request.getRemoteAddr();
            visitor.setIpAddress(ipAddress);
            visitor.setTimestamp(new Date());
            visitorId = visitorRepository.save(visitor);
        }
        session.setAttribute("visitorId", visitorId);
        String referrer = request.getHeader("referer");
        String path = request.getRequestURI();
        logPageview(visitorId, path, referrer);
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id);
    }

    private void logPageview(Long visitorId, String path, String referrer) {
        Pageview pageview = new Pageview();
        pageview.setReferrer(referrer);
        pageview.setPath(path);
        pageview.setVisitorId(visitorId);
        if (path.contains("api")) {
            pageview.setRequestType(Types.RequestType.API);
        }
        if (path.equals("/")) {
            pageview.setContentType(Types.ContentType.HOME);
        } else if (path.contains("post")) {
            Long postId = getObjectId(path);
            pageview.setPostId(postId);
            pageview.setContentType(Types.ContentType.POST);
        } else if (path.contains("category")) {
            pageview.setContentType(Types.ContentType.CATEGORY);
        } else if (path.contains("page")) {
            pageview.setContentType(Types.ContentType.PAGE);
        } else if (path.contains("search")) {
            pageview.setContentType(Types.ContentType.SEARCH);
        } else if (path.contains("tag")) {
            pageview.setContentType(Types.ContentType.TAG);
        } else {
            pageview.setContentType(Types.ContentType.ETC);
        }
        pageview.setTimestamp(new Date());
        pageviewRepository.save(pageview);
    }

    private Long getObjectId(String path) {
        String[] stringArr = path.split("/");
        return Long.parseLong(stringArr[stringArr.length - 1]);
    }

    private String getBrowserName(UserAgent userAgent) {
        if (userAgent.family != null) {
            return userAgent.family;
        }
        return null;
    }

    private String getBrowserVersion(UserAgent userAgent) {
        StringBuilder stringBuilder = new StringBuilder();
        if (userAgent.family != null) {
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
            }
            return stringBuilder.toString();
        }
        return null;
    }

    private String getOsName(OS os) {
        if (os.family != null) {
            return os.family;
        }
        return null;
    }

    private String getOsVersion(OS os) {
        StringBuilder stringBuilder = new StringBuilder();
        if (os.family != null) {
            if (os.major != null) {
                stringBuilder.append(os.major);
                if (os.minor != null) {
                    stringBuilder.append('.');
                    stringBuilder.append(os.minor);
                    if (os.patch != null) {
                        stringBuilder.append('.');
                        stringBuilder.append(os.patch);
                        if (os.patchMinor != null)
                            stringBuilder.append('.');
                        {
                            stringBuilder.append(os.patchMinor);
                        }
                    }
                }
            }
            return stringBuilder.toString();
        }
        return null;
    }

}
