package com.indiepost.service;

import com.indiepost.dto.request.PostQuery;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public void update(Post post) {
        postRepository.update(post);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long count(PostQuery query) {
        return postRepository.count(query);
    }

    @Override
    public List<Post> find(int page, int maxResults, boolean isDesc) {
        return postRepository.find(getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> find(PostQuery query, int page, int maxResults, boolean isDesc) {
        return postRepository.find(query, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> find(PostEnum.Status status, int page, int maxResults, boolean isDesc) {
        return postRepository.findByStatus(status, getPageable(page, maxResults, isDesc));
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
