package com.indiepost.service;

import com.indiepost.dto.Action;
import com.indiepost.dto.Pageview;
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
}
