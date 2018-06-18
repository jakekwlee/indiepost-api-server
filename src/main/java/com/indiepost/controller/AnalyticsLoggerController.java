package com.indiepost.controller;

import com.indiepost.dto.analytics.ActionDto;
import com.indiepost.dto.analytics.PageviewDto;
import com.indiepost.service.AnalyticsLoggerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/stat")
public class AnalyticsLoggerController {

    private final AnalyticsLoggerService analyticsLoggerService;

    @Inject
    public AnalyticsLoggerController(AnalyticsLoggerService analyticsLoggerService) {
        this.analyticsLoggerService = analyticsLoggerService;
    }

    @GetMapping("/pageview")
    public void logPageview(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam(name = "n") String appName,
                            @RequestParam(name = "v") String appVersion,
                            @RequestParam(name = "h") String path,
                            @RequestParam(name = "p", required = false) Long postId,
                            @RequestParam(name = "r", required = false) String referrer,
                            @RequestParam(name = "u", required = false) Long userId
    ) throws IOException {
        PageviewDto dto = new PageviewDto();
        dto.setAppName(appName);
        dto.setAppVersion(appVersion);
        dto.setReferrer(referrer);
        dto.setPostId(postId);
        dto.setPath(path);
        dto.setUserId(userId);
        analyticsLoggerService.logPageview(request, response, dto);
    }


    @GetMapping("/action")
    public void logAction(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(name = "n") String appName,
                          @RequestParam(name = "v") String appVersion,
                          @RequestParam(name = "a") String actionType,
                          @RequestParam(name = "l") String label,
                          @RequestParam(name = "h") String path,
                          @RequestParam(name = "i") Integer value,
                          @RequestParam(name = "u", required = false) Long userId
    ) throws IOException {
        ActionDto dto = new ActionDto();
        dto.setAppName(appName);
        dto.setAppVersion(appVersion);
        dto.setPath(path);
        dto.setActionType(actionType);
        dto.setLabel(label);
        dto.setValue(value);
        dto.setUserId(userId);
        analyticsLoggerService.logAction(request, response, dto);
    }
}
