package com.indiepost.service;

import com.indiepost.dto.Action;
import com.indiepost.dto.Pageview;
import com.indiepost.dto.StatResult;
import com.indiepost.enums.Types.Period;
import com.indiepost.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 13.
 */
public interface StatService {

    Visitor findVisitorById(Long id);

    void logPageview(HttpServletRequest request, Pageview pageview) throws IOException;

    void logAction(HttpServletRequest request, Action action) throws IOException;

    List<StatResult> getPageviews(Date since, Date until, Period period);

    List<StatResult> getVisitors(Date since, Date until, Period period);
}
