package com.indiepost.interceptor;

import com.indiepost.service.SiteStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by jake on 17. 4. 5.
 */
@Component
public class StatLoggingInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private SiteStatService siteStatService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgentString = request.getHeader("User-Agent");
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgentString);
        System.out.println("***************************************************************");
        System.out.println("Requested URI: " + request.getRequestURI());
        System.out.println(client.toString());
        System.out.println("***************************************************************");
        HttpSession session = request.getSession();


        session.setAttribute("visitorId", 1L);
        System.out.println(session.getId());
        return true;
    }
}
