package com.indiepost.service;

import com.indiepost.model.User;
import com.indiepost.viewModel.cms.*;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
public interface ManagementService {
    List<PostMeta> getPublicPosts(int page, int maxResults, boolean descending);

    List<PostMeta> getBookedPosts(int page, int maxResults, boolean descending);

    List<PostMeta> getQueuedPosts(int page, int maxResults, boolean descending);

    List<PostMeta> getDraftPosts(User currentUser, int page, int maxResults, boolean descending);

    List<PostMeta> getDeletedPosts(User currentUser, int page, int maxResults, boolean descending);

    List<TagMeta> getAllTags();

    List<UserMeta> getAllAuthors();

    UserMeta getUserMetaFromUser(User user);

    List<CategoryMeta> getAllCategories();

    PaginationMeta getPagination();

    AdminInitialResponse getInitialState();
}
