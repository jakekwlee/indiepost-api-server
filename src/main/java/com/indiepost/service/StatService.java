package com.indiepost.service;

import com.indiepost.dto.stat.Action;
import com.indiepost.dto.stat.Pageview;
import com.indiepost.dto.stat.PeriodDto;
import com.indiepost.dto.stat.SiteStats;
import com.indiepost.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 17. 4. 13.
 */
public interface StatService {

    Visitor findVisitorById(Long id);

    void logPageview(HttpServletRequest request, Pageview pageview) throws IOException;

    void logAction(HttpServletRequest request, Action action) throws IOException;

    SiteStats getStats(PeriodDto periodDto);
}
