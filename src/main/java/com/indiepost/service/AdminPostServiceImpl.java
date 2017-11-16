package com.indiepost.service;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.model.legacy.LegacyPost;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.ProfileRepository;
import com.indiepost.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.mapper.PostMapper.*;

/**
 * Created by jake on 17. 1. 14.
 */
@Service
@Transactional
public class AdminPostServiceImpl implements AdminPostService {

    private static final Logger log = LoggerFactory.getLogger(AdminPostServiceImpl.class);

    private final AdminPostRepository adminPostRepository;
    private final ProfileRepository profileRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final LegacyPostService legacyPostService;

    @Autowired
    public AdminPostServiceImpl(AdminPostRepository adminPostRepository, UserService userService,
                                LegacyPostService legacyPostService, ProfileRepository profileRepository, TagRepository tagRepository) {
        this.adminPostRepository = adminPostRepository;
        this.userService = userService;
        this.legacyPostService = legacyPostService;
        this.profileRepository = profileRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public void deleteById(Long id) {
        Post post = adminPostRepository.findById(id);
        legacyPostService.deleteById(id);
        adminPostRepository.delete(post);
    }

    // using
    @Override
    public List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = getPageable(page, maxResults, isDesc);
        List<Post> posts = adminPostRepository.find(currentUser, pageable);
        return posts.stream()
                .map(post -> toAdminPostSummaryDto(post))
                .collect(Collectors.toList());
    }

    // no usage
    @Override
    public List<AdminPostSummaryDto> find(PostQuery query) {
        User currentUser = userService.getCurrentUser();
        Pageable pageable = getPageable(query.getPage(), query.getMaxResults(), true);
        List<Post> posts = adminPostRepository.find(currentUser, query, pageable);
        return posts.stream()
                .map(post -> toAdminPostSummaryDto(post))
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
    public AdminPostResponseDto save(AdminPostRequestDto postRequestDto) {
        User currentUser = userService.getCurrentUser();
        Post post = adminPostRequestDtoToPost(postRequestDto);
        post.setStatus(PostStatus.valueOf(postRequestDto.getStatus()));
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        post.setCreator(currentUser);
        post.setModifiedUser(currentUser);

        if (postRequestDto.getProfileIds() != null) {
            List<Profile> profiles = profileRepository.findByIdIn(postRequestDto.getProfileIds());
            addProfilesToPost(post, profiles);
        }
        if (postRequestDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findByIds(postRequestDto.getTagIds());
            addTagsToPost(post, tags);
        }

        adminPostRepository.save(post);
        return toAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosave(AdminPostRequestDto postRequestDto) {
        Post post;
        User currentUser = userService.getCurrentUser();
        if (postRequestDto.getId() != null) {
            Post originalPost = adminPostRepository.findById(postRequestDto.getId());
            post = postToPost(originalPost);
            adminPostRequestDtoToPost(postRequestDto, post);
            post.setOriginal(originalPost);
        } else {
            post = adminPostRequestDtoToPost(postRequestDto);
            post.setCreator(currentUser);
            post.setCreatedAt(LocalDateTime.now());
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
            post.setPublishedAt(LocalDateTime.now().plusDays(7));
        }
        post.setModifiedUser(currentUser);
        post.setModifiedAt(LocalDateTime.now());
        post.setStatus(PostStatus.AUTOSAVE);
        post.setCategoryId(2L);
        adminPostRepository.save(post);
        return toAdminPostResponseDto(post);
    }

    @Override
    public void updateAutosave(Long postId, AdminPostRequestDto postRequestDto) {
        Post post = adminPostRepository.findById(postId);
        adminPostRequestDtoToPost(postRequestDto, post);
        adminPostRepository.update(post);
    }

    @Override
    public AdminPostResponseDto update(Long id, AdminPostRequestDto postRequestDto) {
        Post originalPost = adminPostRepository.findById(id);
        if (postRequestDto.getOriginalId() != null && !postRequestDto.getId().equals(id)) {
            deleteById(postRequestDto.getId());
        }
        PostStatus postDtoStatus = PostStatus.valueOf(postRequestDto.getStatus());
        if (postDtoStatus.equals(PostStatus.AUTOSAVE)) {
            postRequestDto.setStatus(null);
        }
        adminPostRequestDtoToPost(postRequestDto, originalPost);

        PostStatus status = originalPost.getStatus();
        if (status.equals(PostStatus.FUTURE) || status.equals(PostStatus.PUBLISH)) {
            LegacyPost legacyPost = originalPost.getLegacyPost();
            if (legacyPost == null) {
                legacyPost = legacyPostService.save(originalPost);
                originalPost.setLegacyPost(legacyPost);
            } else {
                legacyPostService.update(originalPost);
            }
        }
        adminPostRepository.update(originalPost);
        return toAdminPostResponseDto(originalPost);
    }

    @Override
    public AdminPostResponseDto findOne(Long id) {
        Post post = adminPostRepository.findById(id);
        return toAdminPostResponseDto(post);
    }

    @Override
    public List<AdminPostSummaryDto> getLastUpdated(LocalDateTime dateFrom) {
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
            log.info(String.format("[%s] %s", post.getId(), post.getTitle()));
        }
    }

    @Override
    public List<String> findAllDisplayNames() {
        return adminPostRepository.findAllDisplayNames();
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }

    private AdminPostResponseDto toAdminPostResponseDto(Post post) {
        AdminPostResponseDto responseDto = new AdminPostResponseDto();
        responseDto.setId(post.getId());
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setExcerpt(post.getExcerpt());
        responseDto.setDisplayName(post.getDisplayName());
        responseDto.setTitleImage(post.getTitleImage());
        responseDto.setTitleImageId(post.getTitleImageId());
        responseDto.setStatus(post.getStatus().toString());

        responseDto.setCreatedAt(post.getCreatedAt());
        responseDto.setModifiedAt(post.getModifiedAt());
        responseDto.setPublishedAt(post.getPublishedAt());

        responseDto.setCategoryId(post.getCategoryId());
        responseDto.setCreatorId(post.getCreatorId());
        responseDto.setModifiedUserId(post.getModifiedUserId());

        responseDto.setPicked(post.isPicked());
        responseDto.setFeatured(post.isFeatured());
        responseDto.setSplash(post.isSplash());

        responseDto.setBookmarkCount(post.getBookmarkCount());

        if (post.getOriginalId() != null) {
            responseDto.setOriginalId(post.getOriginalId());
        }
        if (!post.getTags().isEmpty()) {
            List<Long> tagIds = post.getPostTags().stream()
                    .map(postTag -> postTag.getId().getTagId())
                    .collect(Collectors.toList());
            responseDto.setTagIds(tagIds);
        }
        if (!post.getPostProfiles().isEmpty()) {
            List<Long> profileIds = post.getPostProfiles().stream()
                    .map(postProfile -> postProfile.getId().getProfileId())
                    .collect(Collectors.toList());
            responseDto.setProfileIds(profileIds);
        }
        if (post.getTitleImage() != null) {
            ImageSet titleTimageSet = post.getTitleImage();
            titleTimageSet.getImages();
            responseDto.setTitleImage(titleTimageSet);
        }
        return responseDto;
    }

    private AdminPostSummaryDto toAdminPostSummaryDto(Post post) {
        AdminPostSummaryDto postSummaryDto = new AdminPostSummaryDto();
        postSummaryDto.setId(post.getId());
        postSummaryDto.setCreatorName(post.getCreator().getDisplayName());
        postSummaryDto.setModifiedUserName(post.getModifiedUser().getDisplayName());
        postSummaryDto.setCategoryName(post.getCategory().getName());

        postSummaryDto.setStatus(post.getStatus().toString());
        postSummaryDto.setTitle(post.getTitle());
        postSummaryDto.setDisplayName(post.getDisplayName());
        postSummaryDto.setCreatedAt(post.getCreatedAt());
        postSummaryDto.setPublishedAt(post.getPublishedAt());
        postSummaryDto.setModifiedAt(post.getModifiedAt());
        postSummaryDto.setBookmarkCount(post.getBookmarkCount());

        postSummaryDto.setSplash(post.isSplash());
        postSummaryDto.setFeatured(post.isFeatured());
        postSummaryDto.setPicked(post.isPicked());
        return postSummaryDto;
    }
}
