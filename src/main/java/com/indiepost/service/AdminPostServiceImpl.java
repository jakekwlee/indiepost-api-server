package com.indiepost.service;

import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.ContributorRepository;
import com.indiepost.repository.TagRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.*;
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

        if (postRequestDto.getContributors() != null) {
            List<Contributor> contributors =
                    contributorRepository.findByNameIn(postRequestDto.getContributors());
            List<String> contributorNames = contributors.stream().map(c -> c.getName()).collect(Collectors.toList());
            List<String> subList = (List<String>) CollectionUtils.subtract(
                    postRequestDto.getContributors(), contributorNames
            );
            if (!subList.isEmpty()) {
                for (String name : subList) {
                    contributorRepository.save(new Contributor(name));
                }
                contributors = contributorRepository.findByNameIn(postRequestDto.getContributors());

            }
            addContributorsToPost(post, contributors);
        }
        if (postRequestDto.getTags() != null) {
            List<Tag> tags = tagRepository.findByNameIn(postRequestDto.getTags());
            List<String> tagNames = tags.stream().map(t -> t.getName()).collect(Collectors.toList());
            List<String> subList = (List<String>) CollectionUtils.subtract(
                    postRequestDto.getTags(), tagNames
            );
            if (!subList.isEmpty()) {
                for (String name : subList) {
                    tagRepository.save(new Tag(name));
                }
                tags = tagRepository.findByNameIn(postRequestDto.getTags());
            }
            addTagsToPost(post, tags);
        }
    }

    @Override
    public void deleteById(Long id) {
        Post post = adminPostRepository.findOne(id);
        adminPostRepository.delete(post);
    }

    // using
    @Override
    public Page<AdminPostSummaryDto> find(PostStatus status, Pageable pageable) {
        User currentUser = userService.findCurrentUser();
        Pageable pageRequest = getPageable(pageable.getPageNumber(), pageable.getPageSize(), true);
        List<AdminPostSummaryDto> result = adminPostRepository.find(currentUser, status, pageRequest);
        Long count = adminPostRepository.count(status, currentUser);
        if (result.isEmpty()) {
            return new PageImpl<>(result, pageRequest, count);
        }
        return new PageImpl<>(result, pageRequest, count);
    }

    @Override
    public Page<AdminPostSummaryDto> fullTextSearch(String text, PostStatus status,
                                                    Pageable pageable) {
        User currentUser = userService.findCurrentUser();
        Pageable pageRequest = getPageable(pageable.getPageNumber(), pageable.getPageSize(), true);
        List<PostEs> postEsList = postEsRepository.search(text, status, currentUser, pageRequest);
        Integer count = postEsRepository.count(text, status, currentUser);

        if (postEsList.isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageRequest, count);
        }

        List<Long> ids = postEsList.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());

        List<AdminPostSummaryDto> dtoList = adminPostRepository.findByIdIn(ids);
        int index = 0;
        for (AdminPostSummaryDto dto : dtoList) {
            PostEs postEs = postEsList.get(index);
            Highlight highlight = new Highlight();
            boolean highlightExist = false;
            if (postEs.getTitle() != null) {
                highlight.setTitle(postEs.getTitle());
                highlightExist = true;
            }
            if (postEs.getBylineName() != null) {
                highlight.setBylineName(postEs.getBylineName());
                highlightExist = true;
            }
            if (postEs.getCategoryName() != null) {
                highlight.setCategoryName(postEs.getCategoryName());
                highlightExist = true;
            }
            if (postEs.getCreatorName() != null) {
                highlight.setCreatorName(postEs.getCreatorName());
                highlightExist = true;
            }
            if (postEs.getModifiedUserName() != null) {
                highlight.setModifiedUserName(postEs.getModifiedUserName());
                highlightExist = true;
            }
            if (highlightExist) {
                dto.setHighlight(highlight);
            }
            index++;
        }
        return new PageImpl<>(dtoList, pageRequest, count);
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

        if (post.getOriginalId() != null) {
            responseDto.setOriginalId(post.getOriginalId());
        }
        if (post.getTitleImageId() != null) {
            ImageSetDto imageSetDto = imageSetToDto(post.getTitleImage());
            responseDto.setTitleImage(imageSetDto);
        }
        if (!post.getPostTags().isEmpty()) {
            List<String> tags = post.getPostTags().stream()
                    .map(postTag -> postTag.getTag().getName())
                    .collect(Collectors.toList());
            responseDto.setTags(tags);
        }
        if (!post.getPostContributors().isEmpty()) {
            List<String> contributors = post.getPostContributors().stream()
                    .map(postContributor -> postContributor.getContributor().getName())
                    .collect(Collectors.toList());
            responseDto.setContributors(contributors);
        }
        return responseDto;
    }
}
