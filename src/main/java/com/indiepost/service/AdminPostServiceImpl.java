package com.indiepost.service;

import com.indiepost.dto.AdminPostRequestDto;
import com.indiepost.dto.AdminPostResponseDto;
import com.indiepost.dto.AdminPostSummaryDto;
import com.indiepost.dto.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.legacy.Contentlist;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.CategoryRepository;
import com.indiepost.service.mapper.PostMapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 17. 1. 14.
 */
@Service
@Transactional
public class AdminPostServiceImpl implements AdminPostService {

    private static final Logger log = LoggerFactory.getLogger(AdminPostServiceImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final AdminPostRepository adminPostRepository;

    private final CategoryRepository categoryRepository;

    private final UserService userService;

    private final PostMapperService postMapperService;

    private final LegacyPostService legacyPostService;

    @Autowired
    public AdminPostServiceImpl(AdminPostRepository adminPostRepository, UserService userService,
                                CategoryRepository categoryRepository, PostMapperService postMapperService, LegacyPostService legacyPostService) {
        this.adminPostRepository = adminPostRepository;
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.postMapperService = postMapperService;
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
    public AdminPostResponseDto getDtoById(Long id) {
        Post post = findById(id);
        if (post == null) {
            return null;
        }
        List<Tag> tagList = post.getTags();
        if (tagList != null) {
            tagList.get(0);
        }
        if (post.getTitleImage() != null) {
            post.getTitleImage().getImages();
        }
        return postMapperService.postToAdminPostResponseDto(post);
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
    public List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc) {
        User currentUser = userService.getCurrentUser();
        List<Post> postList = adminPostRepository.find(currentUser, getPageable(page, maxResults, isDesc));
        return postList.stream()
                .map(postMapperService::postToAdminPostSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminPostSummaryDto> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc) {
        User currentUser = userService.getCurrentUser();
        List<Post> postList = adminPostRepository.find(currentUser, query, getPageable(page, maxResults, isDesc));
        return postList.stream()
                .map(postMapperService::postToAdminPostSummaryDto)
                .collect(Collectors.toList());
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
        Post post = postMapperService.adminPostRequestDtoToPost(adminPostRequestDto);
        post.setCreatedAt(new Date());
        post.setModifiedAt(new Date());
        post.setAuthor(currentUser);
        post.setEditor(currentUser);
        save(post);
        return postMapperService.postToAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosave(AdminPostRequestDto adminPostRequestDto) {
        Post post;
        User currentUser = userService.getCurrentUser();
        if (adminPostRequestDto.getId() != null) {
            Post originalPost = findById(adminPostRequestDto.getId());
            post = postMapperService.postToPost(originalPost);
            postMapperService.adminPostRequestDtoToPost(adminPostRequestDto, post);
            post.setOriginal(originalPost);
        } else {
            post = postMapperService.adminPostRequestDtoToPost(adminPostRequestDto);
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
        post.setStatus(PostStatus.AUTOSAVE);
        post.setCategory(categoryRepository.getReference(2L));
        save(post);
        return postMapperService.postToAdminPostResponseDto(post);
    }

    @Override
    public void updateAutosave(Long id, AdminPostRequestDto adminPostRequestDto) {
        Post post = findById(id);
        postMapperService.adminPostRequestDtoToPost(adminPostRequestDto, post);
        update(post);
    }

    @Override
    public AdminPostResponseDto update(Long id, AdminPostRequestDto adminPostRequestDto) {
        Post originalPost = findById(id);
        if (adminPostRequestDto.getOriginalId() != null && !adminPostRequestDto.getId().equals(id)) {
            deleteById(adminPostRequestDto.getId());
        }
        if (PostStatus.valueOf(adminPostRequestDto.getStatus()).equals(
                PostStatus.AUTOSAVE
        )) {
            adminPostRequestDto.setStatus(null);
        }
        postMapperService.adminPostRequestDtoToPost(adminPostRequestDto, originalPost);

        PostStatus status = originalPost.getStatus();
        if (status.equals(PostStatus.FUTURE) || status.equals(PostStatus.PUBLISH)) {
            Contentlist contentlist = originalPost.getLegacyPost();
            if (contentlist == null) {
                contentlist = legacyPostService.save(originalPost);
                originalPost.setLegacyPost(contentlist);
            } else {
                legacyPostService.update(originalPost);
            }
        }
        update(originalPost);
        return postMapperService.postToAdminPostResponseDto(originalPost);
    }

    @Override
    public AdminPostResponseDto getPostResponse(Long id) {
        return postMapperService.postToAdminPostResponseDto(findById(id));
    }

    @Override
    public List<AdminPostSummaryDto> getAdminPostTableDtoList(int page, int maxResults, boolean isDesc) {
        User currentUser = userService.getCurrentUser();
        List<Post> postList = adminPostRepository.find(currentUser, getPageable(page, maxResults, isDesc));

        return postList.stream()
                .map(postMapperService::postToAdminPostSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdminPostSummaryDto> getLastUpdated(Date dateFrom) {
        return null;
    }

    @Override
    public void publishScheduledPosts() {
        List<Post> posts = adminPostRepository.findScheduledPosts();
        if (posts == null) {
            return;
        }
        for (Post post : posts) {
            if (post.isSplash()) {
                adminPostRepository.disableSplashPosts();
            }
            if (post.isFeatured()) {
                adminPostRepository.disableFeaturedPosts();
            }
            post.setStatus(PostStatus.PUBLISH);
            adminPostRepository.update(post);
            log.info(dateFormat.format(new Date()) + ": Publish Scheduled Post - " + post.getId());
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
