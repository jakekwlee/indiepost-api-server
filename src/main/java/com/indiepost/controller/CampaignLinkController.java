package com.indiepost.controller;

import com.indiepost.service.AnalyticsLoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Created by jake on 8/7/17.
 */
@Controller
@RequestMapping("/link")
public class CampaignLinkController {
    private final AnalyticsLoggerService analyticsLoggerService;

    @Autowired
    public CampaignLinkController(AnalyticsLoggerService analyticsLoggerService) {
        this.analyticsLoggerService = analyticsLoggerService;
    }

    @GetMapping("/{uid}")
    @ResponseBody
    public String link(@PathVariable String uid, HttpServletRequest req, HttpServletResponse res, Principal principal) throws IOException {
        return "redirect:" + analyticsLoggerService.logClickAndGetLink(req, res, principal, uid);
    }
}