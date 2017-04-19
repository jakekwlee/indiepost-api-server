package com.indiepost.controller.api;

import com.indiepost.dto.PageviewDto;
import com.indiepost.service.SiteStatService;
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
    private final SiteStatService siteStatService;

    @Autowired
    public StatController(SiteStatService siteStatService) {
        this.siteStatService = siteStatService;
    }

    @PostMapping("/pageview")
    public void logPageview(HttpServletRequest request, @RequestBody PageviewDto dto) throws IOException {
        siteStatService.log(request, dto);
    }

}
