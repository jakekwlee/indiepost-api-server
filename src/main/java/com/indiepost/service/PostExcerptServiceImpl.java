package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import com.indiepost.repository.PostExcerptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jake on 11/5/16.
 */
@Service
@Transactional(readOnly = true)
public class PostExcerptServiceImpl implements PostExcerptService {

    private PostExcerptRepository postExcerptRepository;

    private UserService userService;

    @Autowired
    public PostExcerptServiceImpl(UserService userService, PostExcerptRepository postExcerptRepository) {
        this.userService = userService;
        this.postExcerptRepository = postExcerptRepository;
    }

    @Override
    public Post findById(Long id) {
        return postExcerptRepository.findById(id);
    }

    @Override
    public List<Post> findAll(int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        User currentUser = userService.getCurrentUser();
        postExcerptRepository.findAll(currentUser.getId(), pageable);
        return null;
    }

    @Override
    public List<Post> findByTitleLikes(String searchString, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByTitleLikes(searchString, pageable);
    }

    @Override
    public List<Post> findByContentLikes(String searchString, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByContentLikes(searchString, pageable);
    }

    @Override
    public List<Post> findByTitleLikesOrContentLikes(String searchString, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByTitleLikesOrContentLikes(searchString, pageable);
    }

    @Override
    public List<Post> findByTagName(String tagName, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByTagName(tagName, pageable);
    }

    @Override
    public List<Post> findByTagIds(List<Long> tagIds, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByTagIds(tagIds, pageable);
    }

    @Override
    public List<Post> findByStatus(PostEnum.Status status, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByStatus(status, pageable);
    }

    @Override
    public List<Post> findByCategorySlug(String categorySlug, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByCategorySlug(categorySlug, pageable);
    }

    @Override
    public List<Post> findByCategoryId(String categoryId, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public List<Post> findByAuthorName(String authorName, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByAuthorName(authorName, pageable);
    }

    @Override
    public List<Post> findByAuthorId(Long authorID, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByAuthorId(authorID, pageable);
    }

    @Override
    public List<Post> findByStatusAndAuthorId(PostEnum.Status status, Long authorId, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByStatusAndAuthorId(status, authorId, pageable);
    }

    @Override
    public List<Post> findByConditions(PostEnum.Status status, Long authorId, Long categoryId, List<Long> tagIds, String searchText, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postExcerptRepository.findByConditions(status, authorId, categoryId, tagIds, searchText, pageable);
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new PageRequest(page, maxResults, direction, "publishedAt");
    }
}
