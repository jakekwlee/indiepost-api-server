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
import com.indiepost.repository.utils.PostReference;
import org.apache.commons.collections4.CollectionUtils;
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
        User user = userService.findCurrentUser();
        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        post.setPublishedAt(LocalDateTime.now().plusDays(10));
        post.setStatus(PostStatus.AUTOSAVE);
        post.setAuthor(user);
        post.setEditor(user);

        PostReference reference = new PostReference();
        reference.setCategoryId(2L);
        adminPostRepository.saveWithReference(post, reference);
        return toAdminPostResponseDto(post);
    }

    @Override
    public AdminPostResponseDto createAutosaveFromPost(Long postId) {
        Post originalPost = adminPostRepository.findOne(postId);
        if (postId == null) {
            throw new BadRequestException("No original post with id:" + postId);
        }
        Post autosave = duplicate(originalPost);
        autosave.setOriginal(originalPost);
        autosave.setOriginalId(originalPost.getId());
        autosave.setStatus(PostStatus.AUTOSAVE);

        PostReference reference = new PostReference();
        reference.setAuthorId(originalPost.getAuthorId());
        reference.setEditorId(originalPost.getEditorId());
        reference.setCategoryId(originalPost.getCategoryId());
        reference.setTitleImageId(originalPost.getTitleImageId());

        if (originalPost.getContributors() != null) {
            List<Contributor> contributors = originalPost.getContributors();
            addContributorsToPost(autosave, contributors);
        }
        if (originalPost.getTags() != null) {
            List<Tag> tags = originalPost.getTags();
            addTagsToPost(autosave, tags);
        }

        adminPostRepository.saveWithReference(autosave, reference);
        return toAdminPostResponseDto(autosave);
    }

    @Override
    public AdminPostResponseDto createDraft(AdminPostRequestDto dto) {
        User currentUser = userService.findCurrentUser();
        Post post = copyDtoToPost(dto);
        PostReference reference = new PostReference();
        reference.setAuthorId(currentUser.getId());
        reference.setEditorId(currentUser.getId());
        if (dto.getTitleImageId() != null) {
            reference.setTitleImageId(dto.getTitleImageId());
        }
        if (dto.getCategoryId() != null) {
            reference.setCategoryId(dto.getCategoryId());
        }
        reference.setTitleImageId(dto.getTitleImageId());

        adminPostRepository.saveWithReference(post, reference);
        return toAdminPostResponseDto(post);
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
        post.setEditor(currentUser);
        post.setModifiedAt(LocalDateTime.now());

        copyDtoToPost(postRequestDto, post);

        if (postRequestDto.getContributors() != null) {
            Page<Contributor> page =
                    contributorRepository.findByFullNameIn(postRequestDto.getContributors(), PageRequest.of(0, 100));
            List<Contributor> contributors = page.getContent();
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
        adminPostRepository.deleteById(id);
    }

    @Override
    public void delete(Post post) {
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
        List<Long> ids = adminPostRepository.findIds(currentUser, status);
        bulkDeleteByIds(ids);
    }

    @Override
    public void bulkDeleteByIds(List<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Override
    public void bulkStatusUpdate(List<Long> ids, PostStatus changeTo) {
        User currentUser = userService.findCurrentUser();
        if (changeTo.equals(PostStatus.AUTOSAVE)) {
            return;
        }
        for (Long id : ids) {
            Post post = adminPostRepository.findOne(id);
            if (post.getTitleImageId() == null && isPublicStatus(changeTo)) {
                return;
            }
            // if original post is exists, unlink original
            if (post.getOriginalId() != null) {
                post.setOriginalId(null);
            }
            post.setStatus(changeTo);
            post.setEditorId(currentUser.getId());

            LocalDateTime now = LocalDateTime.now();
            post.setModifiedAt(now);
            adminPostRepository.save(post);
        }
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                PageRequest.of(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                PageRequest.of(page, maxResults, Sort.Direction.ASC, "publishedAt");
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
        responseDto.setDisplayName(post.getDisplayName());
        responseDto.setTitleImageId(post.getTitleImageId());
        responseDto.setStatus(post.getStatus().toString());

        responseDto.setCreatedAt(post.getCreatedAt());
        responseDto.setModifiedAt(post.getModifiedAt());
        responseDto.setPublishedAt(post.getPublishedAt());

        responseDto.setCategoryId(post.getCategoryId());
        responseDto.setAuthorId(post.getAuthorId());
        responseDto.setEditorId(post.getEditorId());

        responseDto.setPicked(post.isPicked());
        responseDto.setFeatured(post.isFeatured());
        responseDto.setSplash(post.isSplash());
        Category category = post.getCategory();
        responseDto.setCategoryName(category.getName());

        if (post.getOriginalId() != null) {
            responseDto.setOriginalId(post.getOriginalId());
        }
        if (post.getTitleImage() != null) {
            ImageSetDto imageSetDto = imageSetToDto(post.getTitleImage());
            responseDto.setTitleImage(imageSetDto);
        }
        if (!post.getTags().isEmpty()) {
            List<String> tags = post.getTags().stream()
                    .map(tag -> tag.getName())
                    .collect(Collectors.toList());
            responseDto.setTags(tags);
        }
        if (!post.getContributors().isEmpty()) {
            List<String> contributors = post.getContributors().stream()
                    .map(contributor -> contributor.getFullName())
                    .collect(Collectors.toList());
            responseDto.setContributors(contributors);
        }
        return responseDto;
    }
}
