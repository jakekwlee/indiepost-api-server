package com.indiepost.service;

/**
 * Created by jake on 17. 5. 25.
 */

import com.indiepost.dto.stat.ActionDto;
import com.indiepost.dto.stat.PageviewDto;
import com.indiepost.model.analytics.Visitor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

public interface AnalyticsLoggerService {
    Visitor findVisitorById(Long id);

    void logPageview(HttpServletRequest req, HttpServletResponse res, PageviewDto pageviewDto) throws IOException;

    void logAction(HttpServletRequest req, HttpServletResponse res, ActionDto actionDto) throws IOException;

    String logClickAndGetLink(HttpServletRequest req, HttpServletResponse res, Principal principal, String linkUid) throws IOException;
}
