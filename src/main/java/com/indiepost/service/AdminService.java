package com.indiepost.service;

import com.indiepost.responseModel.admin.*;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface AdminService {
    List<SimplifiedPost> getAllPostsMeta(int page, int maxResults, boolean isDesc);

    List<SimplifiedTag> getAllTagsMeta();

    List<SimplifiedUser> getAllAuthorsMeta();

    List<SimplifiedUser> getAllUsersMeta(int page, int maxResults, boolean isDesc);

    SimplifiedUser getCurrentUser();

    List<SimplifiedCategory> getAllCategoriesMeta();

    InitialResponse getMetaInformation();
}
