package com.indiepost.service;

import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.model.legacy.LegacyPost;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.ContributorRepository;
import com.indiepost.repository.TagRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.enums.Types.isPublicStatus;
import static com.indiepost.mapper.PostMapper.*;

/**
 * Created by jake on 17. 1. 14.
 */
@Service
@Transactional
public class AdminPostServiceImpl implements AdminPostService {

    private final UserService userService;

    private final AdminPostRepository adminPostRepository;

    private final ContributorRepository contributorRepository;

    private final TagRepository tagRepository;

    private final PostEsRepository postEsRepository;

    @Inject
    public AdminPostServiceImpl(UserService userService,
                                AdminPostRepository adminPostRepository,
                                ContributorRepository contributorRepository,
                                PostEsRepository postEsRepository, TagRepository tagRepository) {
        this.userService = userService;
        this.adminPostRepository = adminPostRepository;
        this.contributorRepository = contributorRepository;
        this.postEsRepository = postEsRepository;
        this.tagRepository = tagRepository;
    }


    @Override
    public AdminPostResponseDto findOne(Long id) {
        Post post = adminPostRepository.findOne(id);
        return toAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosave() {
        Post post = new Post();
        Long userId = userService.findCurrentUser().getId();
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        post.setPublishedAt(LocalDateTime.now().plusDays(10));
        post.setStatus(PostStatus.AUTOSAVE);
        post.setCreatorId(userId);
        post.setModifiedUserId(userId);
        adminPostRepository.save(post);
        return toAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosave(Long postId) {
        Post originalPost = adminPostRepository.findOne(postId);
        if (postId == null) {
            throw new BadRequestException("No original post with id:" + postId);
        }
        Post autosave = duplicate(originalPost);
        autosave.setOriginalId(postId);
        autosave.setStatus(PostStatus.AUTOSAVE);
        adminPostRepository.save(autosave);

        if (originalPost.getPostContributors() != null) {
            List<Contributor> contributors = originalPost.getContributors();
            addContributorsToPost(autosave, contributors);
        }
        if (originalPost.getPostTags() != null) {
            List<Tag> tags = originalPost.getTags();
            addTagsToPost(autosave, tags);
        }
        if (autosave.getTitleImageId() != null) {
            ImageSet titleImage = originalPost.getTitleImage();
            autosave.setTitleImage(titleImage);
        }
        adminPostRepository.flush();
        return toAdminPostResponseDto(autosave);
    }

    @Override
    public void update(AdminPostRequestDto postRequestDto) {
        Long postId = postRequestDto.getId();
        Long originalId = postRequestDto.getOriginalId();
        PostStatus status = PostStatus.valueOf(postRequestDto.getStatus());
        Post post;

        if (isPublicStatus(status) && originalId != null) {
            post = adminPostRepository.findOne(originalId);
            adminPostRepository.deleteById(postId);
        } else {
            post = adminPostRepository.findOne(postId);
        }

        User currentUser = userService.findCurrentUser();
        post.setModifiedUserId(currentUser.getId());
        post.setModifiedAt(LocalDateTime.now());

        copyDtoToPost(postRequestDto, post);

        if (postRequestDto.getContributorIds() != null) {
            List<Contributor> contributors =
                    contributorRepository.findByIdIn(postRequestDto.getContributorIds());
            addContributorsToPost(post, contributors);
        }
        if (postRequestDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findByIdIn(postRequestDto.getTagIds());
            addTagsToPost(post, tags);
        }

        LegacyPost legacyPost = post.getLegacyPost();
        if (isPublicStatus(post.getStatus())) {
            if (legacyPost == null) {
                legacyPost = new LegacyPost();
                post.setLegacyPost(legacyPost);
            }
            legacyPost.setProperties(post);
        } else {
            if (legacyPost == null) {
                return;
            }
            post.setLegacyPost(null);
        }
    }

    @Override
    public void deleteById(Long id) {
        Post post = adminPostRepository.findOne(id);
        adminPostRepository.delete(post);
    }

    // using
    @Override
    public List<AdminPostSummaryDto> find(int page, int maxResults, boolean isDesc) {
        User currentUser = userService.findCurrentUser();
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return adminPostRepository.find(currentUser, pageable);
    }

    // no usage
    @Override
    public List<AdminPostSummaryDto> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc) {
        User currentUser = userService.findCurrentUser();
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return adminPostRepository.find(currentUser, query, pageable);
    }

    public List<AdminPostResponseDto> fullTextSearch(String text, PostStatus status,
                                                     int page, int maxResults, boolean isDesc) {
        Pageable pageable = new PageRequest(page, maxResults);
        List<PostEs> postEsList = postEsRepository.search(text, status.toString(), pageable);

        if (postEsList.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> ids = postEsList.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());

        List<AdminPostSummaryDto> dtoList = null;
        // TODO
        return null;
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
    public List<AdminPostSummaryDto> findLastUpdated(LocalDateTime dateFrom) {
        return null;
    }

    @Override
    public List<String> findAllBylineNames() {
        return adminPostRepository.findAllDisplayNames();
    }

    @Override
    public void bulkDeleteByStatus(PostStatus status) {
        User currentUser = userService.findCurrentUser();
        if (isPublicStatus(status)) {
            throw new BadRequestException(
                    "It's prohibited directly bulk delete public status posts(PUBLIC|FUTURE|PENDING).");
        }
        adminPostRepository.bulkDeleteByUserAndStatus(currentUser, status);
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }

    private AdminPostResponseDto toAdminPostResponseDto(Post post) {
        if (post == null) {
            return null;
        }
        AdminPostResponseDto responseDto = new AdminPostResponseDto();
        responseDto.setId(post.getId());
        responseDto.setTitle(post.getTitle());
        responseDto.setContent(post.getContent());
        responseDto.setExcerpt(post.getExcerpt());
        responseDto.setBylineName(post.getBylineName());
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
        if (post.getTitleImageId() != null) {
            ImageSetDto imageSetDto = imageSetToDto(post.getTitleImage());
            responseDto.setTitleImage(imageSetDto);
        }
        if (!post.getPostTags().isEmpty()) {
            List<Long> tagIds = post.getPostTags().stream()
                    .map(postTag -> postTag.getTag().getId())
                    .collect(Collectors.toList());
            responseDto.setTagIds(tagIds);
        }
        if (!post.getPostContributors().isEmpty()) {
            List<Long> contributorIds = post.getPostContributors().stream()
                    .map(postContributor -> postContributor.getContributor().getId())
                    .collect(Collectors.toList());
            responseDto.setContributorIds(contributorIds);
        }
        return responseDto;
    }
}
