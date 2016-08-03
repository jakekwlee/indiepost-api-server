package com.indiepost.service;

import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import com.indiepost.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post findById(int id) {
        return postRepository.findById(id);
    }

    @Override
    public Post findByIdForUser(int id) {
        return postRepository.findByIdForUser(id);
    }

    @Override
    public void update(Post post) {
        postRepository.update(post);
    }

    @Override
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public List<Post> findAll(int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findAllOrderByAsc(int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategory(Category category, int page, int maxResults) {
        page = normalizePage(page);
        return postRepository
                .findByCategory(category, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategoryOrderByAsc(Category category, int page, int maxResults) {
        page = normalizePage(page);
        return postRepository
                .findByCategory(category, new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategorySlug(String slug, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByCategorySlugOrderByAsc(String slug, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByAuthor(User user, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByAuthorOrderByAsc(User user, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByEditor(User editor, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByEditorByAsc(User editor, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByStatus(Post.Status status, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findByStatusOrderByAsc(Post.Status status, int page, int maxResults) {
        page = normalizePage(page);
        return null;
    }

    @Override
    public List<Post> findAllForUser(int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findAllForUser(new PageRequest(page, maxResults, Sort.Direction.DESC, "id"));
    }

    @Override
    public List<Post> findByCategoryForUser(Category category, int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findByCategoryForUser(category, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategorySlugForUser(String slug, int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findByCategorySlugForUser(slug, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByAuthoUsernamerForUser(String username, int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findByAuthorUsernameForUser(username, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByAuthorForUser(User author, int page, int maxResults) {
        page = normalizePage(page);
        return postRepository.findByAuthorForUser(author, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    private int normalizePage(int page) {
        page = page < 1 ? 0 : page - 1;
        return page;
    }
}
