package com.indiepost.service;

import com.indiepost.viewModel.cms.*;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface ManagementService {
    List<PostMeta> getAllPostsMeta(int page, int maxResults, boolean isDesc);

    List<TagMeta> getAllTagsMeta();

    List<UserMeta> getAllAuthorsMeta();

    List<UserMeta> getAllUsersMeta(int page, int maxResults, boolean isDesc);

    UserMeta getCurrentUser();

    List<CategoryMeta> getAllCategoriesMeta();

    MetaInformation getMetaInformation();
}
