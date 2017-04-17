package com.indiepost.service;

import com.indiepost.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 17. 4. 13.
 */
public interface SiteStatService {

    void log(HttpServletRequest request) throws IOException;

    Visitor findVisitorById(Long id);

}
