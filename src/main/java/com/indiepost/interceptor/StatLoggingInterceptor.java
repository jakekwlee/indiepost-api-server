package com.indiepost.interceptor;

import com.indiepost.service.SiteStatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by jake on 17. 4. 5.
 */
@Component
public class StatLoggingInterceptor extends HandlerInterceptorAdapter {

    private final SiteStatService siteStatService;

    @Autowired
    public StatLoggingInterceptor(SiteStatService siteStatService) {
        super();
        this.siteStatService = siteStatService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgentString = request.getHeader("User-Agent");
        HttpSession session = request.getSession();
        siteStatService.log(request);
        System.out.println("***************************************************************");
        System.out.println("Requested URI: " + request.getRequestURI());
        System.out.println("User-Agent" + userAgentString);
        System.out.println("Visitor ID:" + session.getAttribute("visitorId"));
        System.out.println("***************************************************************");

        return true;
    }
}
