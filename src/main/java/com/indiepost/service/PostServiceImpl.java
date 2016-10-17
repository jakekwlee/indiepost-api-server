package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
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

    @Autowired
    private UserService userService;

    @Override
    public void save(Post post) {
        postRepository.save(post);
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
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long countPublished() {
        return postRepository.count(PostEnum.Status.PUBLISHED);
    }

    @Override
    public Long countBooked() {
        return postRepository.count(PostEnum.Status.BOOKED);
    }

    @Override
    public Long countQueued() {
        return postRepository.count(PostEnum.Status.QUEUED);
    }

    @Override
    public Long countDraft() {
        String username = userService.getCurrentUsername();
        return postRepository.count(PostEnum.Status.DRAFT, username);
    }

    @Override
    public Long countDeleted() {
        String username = userService.getCurrentUsername();
        return postRepository.count(PostEnum.Status.DELETED, username);
    }

    @Override
    public List<Post> findAll(int page, int maxResults) {
        return postRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findAllOrderByAsc(int page, int maxResults) {
        return postRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findAll(PostEnum.Status status, User author, Category category, int page, int maxResults) {
        return postRepository.findAll(status, author.getId(), category.getId(), new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findAllOrderByAsc(PostEnum.Status status, User author, Category category, int page, int maxResults) {
        return postRepository.findAll(status, author.getId(), category.getId(), new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategory(Category category, int page, int maxResults) {
        return postRepository
                .findByCategoryId(category.getId(), new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategoryOrderByAsc(Category category, int page, int maxResults) {
        return postRepository
                .findByCategoryId(category.getId(), new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategorySlug(String slug, int page, int maxResults) {
        return postRepository
                .findByCategorySlug(slug, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByCategorySlugOrderByAsc(String slug, int page, int maxResults) {
        return postRepository
                .findByCategorySlug(slug, new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByAuthor(User user, int page, int maxResults) {
        return postRepository
                .findByAuthorId(user.getId(), new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByAuthorOrderByAsc(User user, int page, int maxResults) {
        return postRepository
                .findByAuthorId(user.getId(), new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByAuthorName(String authorName, int page, int maxResults) {
        return postRepository
                .findByAuthorName(authorName, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByAuthorNameOrderByAsc(String authorName, int page, int maxResults) {
        return postRepository
                .findByAuthorName(authorName, new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByStatus(PostEnum.Status status, int page, int maxResults) {
        return postRepository
                .findByStatus(status, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByStatusOrderByAsc(PostEnum.Status status, int page, int maxResults) {
        return postRepository
                .findByStatus(status, new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByTag(Tag tag, int page, int maxResults) {
        return postRepository
                .findByTagId(tag.getId(), new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByTagOrderByAsc(Tag tag, int page, int maxResults) {
        return postRepository
                .findByTagId(tag.getId(), new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }

    @Override
    public List<Post> findByTagName(String tagName, int page, int maxResults) {
        return postRepository
                .findByTagName(tagName, new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt"));
    }

    @Override
    public List<Post> findByTagOrderByAsc(String tagName, int page, int maxResults) {
        return postRepository
                .findByTagName(tagName, new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt"));
    }
}
