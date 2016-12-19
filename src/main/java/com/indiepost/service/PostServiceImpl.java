package com.indiepost.service;

import com.indiepost.enums.PostEnum;
import com.indiepost.mapper.PostMapper;
import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.repository.PostRepository;
import dto.request.AdminPostRequestDto;
import dto.response.AdminPostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
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

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private LegacyPostService legacyPostService;

    @Override
    public Long save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public AdminPostResponseDto save(AdminPostRequestDto adminPostRequestDto) {
        User currentUser = userService.getCurrentUser();
        Post post = postMapper.adminPostRequestDtoToPost(adminPostRequestDto);
        post.setCreatedAt(new Date());
        post.setModifiedAt(new Date());
        post.setAuthor(currentUser);
        post.setEditor(currentUser);
        save(post);
        return postMapper.postToAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto) {
        Post post;
        User currentUser = userService.getCurrentUser();
        if (adminPostRequestDto.getId() != null) {
            post = findById(adminPostRequestDto.getId());
            Post originalPost = findById(adminPostRequestDto.getId());
            postRepository.detach(post);
            post.setId(null);
            post.setComments(null);
            post.setOriginal(originalPost);
            postMapper.adminPostRequestDtoToPost(adminPostRequestDto, post);
        } else {
            post = postMapper.adminPostRequestDtoToPost(adminPostRequestDto);
            post.setAuthor(currentUser);
            post.setCreatedAt(new Date());
        }
        if (post.getPublishedAt() == null) {
            Date publishDate = Date.from(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC));
            post.setPublishedAt(publishDate);
        }

        post.setEditor(currentUser);
        post.setModifiedAt(new Date());
        post.setStatus(PostEnum.Status.AUTOSAVE);
        save(post);
        return postMapper.postToAdminPostResponseDto(post);
    }

    @Override
    public void updateAutosave(Long id, AdminPostRequestDto adminPostRequestDto) {
        Post post = findById(id);
        postMapper.adminPostRequestDtoToPost(adminPostRequestDto, post);
        update(post);
    }

    @Override
    public AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto) {
        Post originalPost = findById(id);
        if (adminPostRequestDto.getOriginalId() != null && !adminPostRequestDto.getId().equals(id)) {
            deleteById(adminPostRequestDto.getId());
        }
        postRepository.detach(originalPost);
        postMapper.adminPostRequestDtoToPost(adminPostRequestDto, originalPost);

        PostEnum.Status status = PostEnum.Status.valueOf(adminPostRequestDto.getStatus());
        if (status.equals(PostEnum.Status.FUTURE) || status.equals(PostEnum.Status.PUBLISH)) {
            Contentlist contentlist = originalPost.getLegacyPost();
            if (contentlist == null) {
                contentlist = legacyPostService.save(originalPost);
                originalPost.setLegacyPost(contentlist);
            } else {
                legacyPostService.update(originalPost);
            }
        }
        update(originalPost);
        return postMapper.postToAdminPostResponseDto(originalPost);
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
    public void deleteById(Long id) {
        Post post = postRepository.findById(id);
        delete(post);
        legacyPostService.deleteById(id);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long count(PostEnum.Status status) {
        return postRepository.count(status);
    }

    @Override
    public List<Post> findAll(int page, int maxResults, boolean isDesc) {
        return postRepository.findAll(getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findAll(PostEnum.Status status, User author, Category category, int page, int maxResults, boolean isDesc) {
        return postRepository.findAll(status, author.getId(), category.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByCategory(Category category, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByCategoryId(category.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByCategorySlug(String slug, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByCategorySlug(slug, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByAuthor(User user, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByAuthorId(user.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByAuthorName(String authorName, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByAuthorName(authorName, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByStatus(PostEnum.Status status, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByStatus(status, getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByTag(Tag tag, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByTagId(tag.getId(), getPageable(page, maxResults, isDesc));
    }

    @Override
    public List<Post> findByTagName(String tagName, int page, int maxResults, boolean isDesc) {
        return postRepository
                .findByTagName(tagName, getPageable(page, maxResults, isDesc));
    }

    @Override
    public AdminPostResponseDto getPostResponse(Long id) {
        return postMapper.postToAdminPostResponseDto(findById(id));
    }

    @Override
    public void publishPosts() {
        List<Post> posts = postRepository.findPostToPublish();
        for (Post post : posts) {
            post.setStatus(PostEnum.Status.PUBLISH);
            postRepository.save(post);
        }
    }


    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
