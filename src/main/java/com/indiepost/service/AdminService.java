package com.indiepost.service;

import com.indiepost.responseModel.admin.*;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {
    List<SimplifiedPost> getAllSimplifiedPosts(int page, int maxResults, boolean isDesc);

    List<SimplifiedPost> getLastUpdated(Date date);

    List<SimplifiedTag> getAllSimplifiedTags();

    List<SimplifiedUser> getAllSimplifiedAuthors();

    List<SimplifiedUser> getAllUsersMeta(int page, int maxResults, boolean isDesc);

    SimplifiedUser getCurrentUser();

    List<SimplifiedCategory> getAllSimplifiedCategories();

    InitialResponse getInitialResponse();
}
