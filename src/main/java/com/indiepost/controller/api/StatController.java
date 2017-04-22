package com.indiepost.controller.api;

import com.indiepost.dto.Action;
import com.indiepost.dto.Pageview;
import com.indiepost.service.StatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 17. 3. 5.
 */
@RestController
@RequestMapping("/api/stat")
public class StatController {
    private final StatService statService;

    @Autowired
    public StatController(StatService statService) {
        this.statService = statService;
    }

    @PostMapping("/pageview")
    public void logPageview(HttpServletRequest request, @RequestBody Pageview pageview) throws IOException {
        statService.logPageview(request, pageview);
    }

    @PostMapping("/action")
    public void logAction(HttpServletRequest request, @RequestBody Action action) throws IOException {
        statService.logAction(request, action);
    }
}
