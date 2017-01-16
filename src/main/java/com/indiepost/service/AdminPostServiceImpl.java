package com.indiepost.service;

import com.indiepost.dto.request.AdminPostRequestDto;
import com.indiepost.dto.request.PostQuery;
import com.indiepost.dto.response.AdminPostResponseDto;
import com.indiepost.dto.response.AdminPostTableDto;
import com.indiepost.enums.PostEnum;
import com.indiepost.mapper.PostMapper;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 1. 14.
 */
@Service
@Transactional
public class AdminPostServiceImpl implements AdminPostService {

    private final AdminPostRepository adminPostRepository;

    private final UserService userService;

    private final CategoryRepository categoryRepository;

    private final PostMapper postMapper;

    private final LegacyPostService legacyPostService;

    @Autowired
    public AdminPostServiceImpl(AdminPostRepository adminPostRepository, UserService userService,
                                CategoryRepository categoryRepository, PostMapper postMapper, LegacyPostService legacyPostService) {
        this.adminPostRepository = adminPostRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.postMapper = postMapper;
        this.legacyPostService = legacyPostService;
    }

    @Override
    public Long save(Post post) {
        return adminPostRepository.save(post);
    }

    @Override
    public Post findById(Long id) {
        return adminPostRepository.findById(id);
    }

    @Override
    public void update(Post post) {
        adminPostRepository.update(post);
    }

    @Override
    public void delete(Post post) {
        legacyPostService.deleteById(post.getId());
        adminPostRepository.delete(post);
    }

    @Override
    public void deleteById(Long id) {
        Post post = adminPostRepository.findById(id);
        delete(post);
    }

    @Override
    public List<Post> find(int page, int maxResults, boolean isDesc) {
        User currentUser = userService.getCurrentUser();
        return adminPostRepository.find(
                currentUser,
                getPageable(page, maxResults, isDesc)
        );
    }

    @Override
    public List<Post> find(PostQuery query, int page, int maxResults, boolean isDesc) {
        User currentUser = userService.getCurrentUser();
        return adminPostRepository.find(
                currentUser,
                query,
                getPageable(page, maxResults, isDesc)
        );
    }

    @Override
    public Long count() {
        return adminPostRepository.count();
    }

    @Override
    public Long count(PostQuery query) {
        return adminPostRepository.count(query);
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
            Post originalPost = findById(adminPostRequestDto.getId());
            post = postMapper.postToPost(originalPost);
            postMapper.adminPostRequestDtoToPost(adminPostRequestDto, post);
            post.setOriginal(originalPost);
        } else {
            post = postMapper.adminPostRequestDtoToPost(adminPostRequestDto);
            post.setAuthor(currentUser);
            post.setCreatedAt(new Date());
        }
        if (StringUtils.isEmpty(post.getTitle())) {
            post.setTitle("No Title");
        }
        if (StringUtils.isEmpty(post.getContent())) {
            post.setContent("");
        }
        if (StringUtils.isEmpty(post.getExcerpt())) {
            post.setExcerpt("");
        }
        if (StringUtils.isEmpty(post.getDisplayName())) {
            post.setDisplayName("Indiepost");
        }
        if (post.getPublishedAt() == null) {
            Date publishDate = Date.from(LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.UTC));
            post.setPublishedAt(publishDate);
        }
        post.setEditor(currentUser);
        post.setModifiedAt(new Date());
        post.setStatus(PostEnum.Status.AUTOSAVE);
        post.setCategory(categoryRepository.getReference(2L));
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
        if (PostEnum.Status.valueOf(adminPostRequestDto.getStatus()).equals(
                PostEnum.Status.AUTOSAVE
        )) {
            adminPostRequestDto.setStatus(null);
        }
        postMapper.adminPostRequestDtoToPost(adminPostRequestDto, originalPost);

        PostEnum.Status status = originalPost.getStatus();
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
    public AdminPostResponseDto getPostResponse(Long id) {
        return postMapper.postToAdminPostResponseDto(findById(id));
    }

    @Override
    public List<AdminPostTableDto> getAdminPostTableDtoList(int page, int maxResults, boolean isDesc) {
        List<Post> posts = find(page, maxResults, isDesc);
        List<AdminPostTableDto> adminPostTableDtos = new ArrayList<>();
        for (Post post : posts) {
            AdminPostTableDto adminPostTableDto = postMapper.postToAdminPostTableDto(post);
            adminPostTableDtos.add(adminPostTableDto);
        }
        return adminPostTableDtos;
    }

    @Override
    public List<AdminPostTableDto> getLastUpdated(Date dateFrom) {
        return null;
    }

    @Override
    public void publishPosts() {
        List<Post> posts = adminPostRepository.findPostToPublish();
        if (posts == null) {
            return;
        }
        for (Post post : posts) {
            post.setStatus(PostEnum.Status.PUBLISH);
            adminPostRepository.save(post);
        }
    }

    public List<String> findAllDisplayNames() {
        return adminPostRepository.findAllDisplayNames();
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
