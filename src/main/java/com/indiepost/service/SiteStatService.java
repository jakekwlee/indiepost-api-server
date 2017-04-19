package com.indiepost.service;

import com.indiepost.dto.PageviewDto;
import com.indiepost.model.Visitor;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 17. 4. 13.
 */
public interface SiteStatService {

    Visitor findVisitorById(Long id);

    void log(HttpServletRequest request, PageviewDto pageviewDto) throws IOException;
}
