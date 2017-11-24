package com.indiepost.service;

import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostSearch;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.model.legacy.LegacyPost;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.ContributorRepository;
import com.indiepost.repository.MetadataRepository;
import com.indiepost.repository.TagRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
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

import static com.indiepost.mapper.PostMapper.adminPostRequestDtoToPost;
import static com.indiepost.mapper.PostMapper.postToPost;

/**
 * Created by jake on 17. 1. 14.
 */
@Service
@Transactional
public class AdminPostServiceImpl implements AdminPostService {

    private static final Logger log = LoggerFactory.getLogger(AdminPostServiceImpl.class);

    private final UserService userService;
    private final LegacyPostService legacyPostService;
    private final AdminPostRepository adminPostRepository;
    private final ContributorRepository contributorRepository;
    private final TagRepository tagRepository;
    private final MetadataRepository metadataRepository;
    private final PostEsRepository postEsRepository;

    @Autowired
    public AdminPostServiceImpl(UserService userService,
                                LegacyPostService legacyPostService,
                                AdminPostRepository adminPostRepository,
                                ContributorRepository contributorRepository,
                                TagRepository tagRepository,
                                MetadataRepository metadataRepository,
                                PostEsRepository postEsRepository) {
        this.userService = userService;
        this.legacyPostService = legacyPostService;
        this.adminPostRepository = adminPostRepository;
        this.contributorRepository = contributorRepository;
        this.tagRepository = tagRepository;
        this.metadataRepository = metadataRepository;
        this.postEsRepository = postEsRepository;
    }


    @Override
    public AdminPostResponseDto findOne(Long id) {
        Post post = adminPostRepository.findById(id);
        return toAdminPostResponseDto(post);
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
        User currentUser = userService.findCurrentUser();
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return adminPostRepository.find(currentUser, pageable);
    }

    // no usage
    @Override
    public List<AdminPostSummaryDto> search(PostSearch search, int page, int maxResults, boolean isDesc) {
        User currentUser = userService.findCurrentUser();
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return adminPostRepository.find(currentUser, search, pageable);
    }

    @Override
    public Long count() {
        return adminPostRepository.count();
    }

    @Override
    public Long count(PostSearch search) {
        return adminPostRepository.count(search);
    }

    @Override
    public AdminPostResponseDto save(AdminPostRequestDto postRequestDto) {
        User currentUser = userService.findCurrentUser();
        Post post = adminPostRequestDtoToPost(postRequestDto);
        post.setStatus(PostStatus.valueOf(postRequestDto.getStatus()));
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        post.setCreator(currentUser);
        post.setModifiedUser(currentUser);

        if (postRequestDto.getContributorIds() != null) {
            List<Contributor> contributors =
                    contributorRepository.findByIdIn(postRequestDto.getContributorIds());
            addContributorsToPost(post, contributors);
        }
        if (postRequestDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findByIdIn(postRequestDto.getTagIds());
            addTagsToPost(post, tags);
        }

        adminPostRepository.save(post);
        return toAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosave(AdminPostRequestDto postRequestDto) {
        Post post;
        User currentUser = userService.findCurrentUser();
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
        if (StringUtils.isEmpty(post.getBylineName())) {
            post.setBylineName("Indiepost");
        }
        if (post.getPublishedAt() == null) {
            post.setPublishedAt(LocalDateTime.now().plusDays(7));
        }
        post.setModifiedUser(currentUser);
        post.setModifiedAt(LocalDateTime.now());
        post.setStatus(PostStatus.AUTOSAVE);
        post.setCategoryId(2L);

        if (postRequestDto.getContributorIds() != null) {
            List<Contributor> contributors =
                    contributorRepository.findByIdIn(postRequestDto.getContributorIds());
            addContributorsToPost(post, contributors);
        }
        if (postRequestDto.getTagIds() != null) {
            List<Tag> tags = tagRepository.findByIdIn(postRequestDto.getTagIds());
            addTagsToPost(post, tags);
        }

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
    public List<AdminPostSummaryDto> findLastUpdated(LocalDateTime dateFrom) {
        return null;
    }

    @Override
    public List<String> findAllBylineNames() {
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
        responseDto.setBylineName(post.getBylineName());
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
        if (!post.getPostContributors().isEmpty()) {
            List<Long> contributorIds = post.getPostContributors().stream()
                    .map(postContributor -> postContributor.getId().getContributorId())
                    .collect(Collectors.toList());
            responseDto.setContributorIds(contributorIds);
        }
        if (post.getTitleImage() != null) {
            ImageSet titleImageSet = post.getTitleImage();
            titleImageSet.getImages();
            responseDto.setTitleImage(titleImageSet);
        }
        return responseDto;
    }

    private void addTagsToPost(Post post, List<Tag> tags) {
        post.getPostTags().clear();
        for (Tag tag : tags) {
            post.addTag(tag);
        }
    }

    private void addContributorsToPost(Post post, List<Contributor> contributors) {
        post.getPostContributors().clear();
        for (Contributor contributor : contributors) {
            post.addContributor(contributor);
        }
    }
}
