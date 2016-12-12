package com.indiepost.service;

import com.indiepost.model.response.*;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {
    List<SimplifiedPost> getAllSimplifiedPosts(int page, int maxResults, boolean isDesc);

    List<SimplifiedPost> getLastUpdated(Date date);

    List<SimplifiedTag> getAllSimplifiedTags();

    List<AdminUserResponse> getAllSimplifiedAuthors();

    List<AdminUserResponse> getAllUsersMeta(int page, int maxResults, boolean isDesc);

    AdminUserResponse getCurrentUser();

    List<SimplifiedCategory> getAllSimplifiedCategories();

    AdminInitialResponse getInitialResponse();
}
