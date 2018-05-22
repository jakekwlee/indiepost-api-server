package com.indiepost.service;

import org.springframework.ui.Model;

/**
 * Created by jake on 17. 5. 13.
 */
public interface ServerSideRenderingService {
    void renderHome(Model model, String servletPath);

    void renderPost(Long postId, Model model, String servletPath);

    void renderPage(String pageSlug, Model model, String servletPath);

    void renderPostsByCategoryPage(String categorySlug, Model model, String servletPath);

    void renderPostByTagPage(String tagName, Model model, String servletPath);

    void renderPostSearchResultsPage(String keyword, Model model, String servletPath);

    void renderPostByContributorPage(String contributorName, Model model, String servletPath);
}
