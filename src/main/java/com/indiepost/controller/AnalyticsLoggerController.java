package com.indiepost.controller;

import com.indiepost.dto.analytics.ActionDto;
import com.indiepost.dto.analytics.PageviewDto;
import com.indiepost.service.AnalyticsLoggerService;
import com.indiepost.service.UserReadService;
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

    private final UserReadService userReadService;

    @Inject
    public AnalyticsLoggerController(AnalyticsLoggerService analyticsLoggerService, UserReadService userReadService) {
        this.analyticsLoggerService = analyticsLoggerService;
        this.userReadService = userReadService;
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

        if (userId != null && postId != null) {
            userReadService.add(userId, postId);
        }
    }


    @GetMapping("/action")
    public void logAction(HttpServletRequest request,
                          HttpServletResponse response,
                          @RequestParam(name = "n") String appName,
                          @RequestParam(name = "v") String appVersion,
                          @RequestParam(name = "a") String actionType,
                          @RequestParam(name = "h") String path,
                          @RequestParam(name = "i", required = false) Integer value,
                          @RequestParam(name = "l", required = false) String label,
                          @RequestParam(name = "u", required = false) Long userId
    ) throws IOException {
        ActionDto dto = new ActionDto.Builder(appName, appVersion, path, actionType)
                .label(label)
                .value(value)
                .userId(userId)
                .build();
        analyticsLoggerService.logAction(request, response, dto);
    }
}
