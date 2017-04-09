package com.indiepost.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import ua_parser.Client;
import ua_parser.Parser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jake on 17. 4. 5.
 */
@Component
public class StatLoggingInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userAgentString = request.getHeader("User-Agent");
        Parser uaParser = new Parser();
        Client client = uaParser.parse(userAgentString);
        System.out.println("***************************************************************");
        System.out.println("Requested URI: " + request.getRequestURI());
        System.out.println(client.toString());
        System.out.println("***************************************************************");
        return true;
    }
}
