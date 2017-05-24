package com.indiepost.service;

/**
 * Created by jake on 17. 5. 25.
 */

import com.indiepost.dto.stat.Action;
import com.indiepost.dto.stat.Pageview;
import com.indiepost.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AnalyticsStatLoggerService {
    Visitor findVisitorById(Long id);

    void logPageview(HttpServletRequest request, Pageview pageview) throws IOException;

    void logAction(HttpServletRequest request, Action action) throws IOException;
}
