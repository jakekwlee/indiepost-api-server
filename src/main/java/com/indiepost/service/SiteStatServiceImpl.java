package com.indiepost.service;

import com.indiepost.model.Visitor;
import com.indiepost.repository.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua_parser.Client;
import ua_parser.OS;
import ua_parser.Parser;
import ua_parser.UserAgent;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 17. 4. 13.
 */
@Service
public class SiteStatServiceImpl implements SiteStatService {

    private final VisitorRepository visitorRepository;

    @Autowired
    public SiteStatServiceImpl(VisitorRepository visitorRepository) {
        this.visitorRepository = visitorRepository;
    }

    @Override
    public Long log(HttpServletRequest request) throws IOException {
        String userAgentString = request.getHeader("User-Agent");
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgentString);
        String osName = getOsName(client.os);
        Visitor visitor = new Visitor();
        if (osName == null) {
            com.indiepost.model.UserAgent userAgent = new com.indiepost.model.UserAgent();
            userAgent.setUaString(client.userAgent.toString());
            userAgent.setUaHash(client.userAgent.toString());
            visitor.setUserAgent(userAgent);
        } else {
            // TODO
        }

        return null;
    }

    @Override
    public Long saveVisitor(Visitor visitor) {
        return visitorRepository.save(visitor);
    }

    @Override
    public Visitor findVisitorById(Long id) {
        return visitorRepository.findById(id);
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
                    stringBuilder.append(userAgent.minor);
                    if (userAgent.patch != null) {
                        stringBuilder.append(userAgent.patch);
                    }
                }
                return stringBuilder.toString();
            }
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
                    stringBuilder.append(os.minor);
                    if (os.patch != null) {
                        stringBuilder.append(os.patch);
                    }
                }
                return stringBuilder.toString();
            }
        }
        return null;
    }
}
