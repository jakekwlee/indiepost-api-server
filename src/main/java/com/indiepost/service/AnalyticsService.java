package com.indiepost.service;

import com.indiepost.dto.stat.*;
import com.indiepost.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
public interface AnalyticsService {

    Visitor findVisitorById(Long id);

    void logPageview(HttpServletRequest request, Pageview pageview) throws IOException;

    void logAction(HttpServletRequest request, Action action) throws IOException;

    SiteStats getStats(PeriodDto periodDto);

    List<PostStat> getPostsOrderByPageviews(PeriodDto periodDto);
}
