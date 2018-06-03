package com.indiepost.service;

import com.amazonaws.services.pinpoint.model.BadRequestException;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.post.AdminPostRequestDto;
import com.indiepost.dto.post.AdminPostResponseDto;
import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Contributor;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.User;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.ContributorRepository;
import com.indiepost.repository.TagRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.enums.Types.isPublicStatus;
import static com.indiepost.mapper.PostMapper.*;
import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

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
    public Long createAutosave(AdminPostRequestDto dto) {
        User currentUser = userService.findCurrentUser();

        Post post = new Post();
        if (dto.getId() != null) {
            Post originalPost = adminPostRepository.findOne(dto.getId());
            post.setOriginal(originalPost);
            post.setOriginalId(originalPost.getId());
            post.setAuthor(post.getAuthor());
        }


        copyDtoToPost(dto, post);

        if (dto.getTitleImageId() != null) {
            post.setTitleImageId(dto.getTitleImageId());
        }
        if (dto.getCategoryId() != null) {
            post.setCategoryId(dto.getCategoryId());
        }

        post.setCreatedAt(LocalDateTime.now());
        post.setModifiedAt(LocalDateTime.now());
        post.setEditor(currentUser);
        if (post.getAuthor() == null) {
            post.setAuthor(currentUser);
        }
        post.setStatus(PostStatus.AUTOSAVE);

        Long postId = adminPostRepository.persist(post);

        addContributors(post, dto.getContributors());
        addTags(post, dto.getTags());

        return postId;
    }

    @Override
    public AdminPostResponseDto findOne(Long id) {
        Post post = adminPostRepository.findOne(id);
        return toAdminPostResponseDto(post);
    }

    @Override
    public Long createDraft(AdminPostRequestDto dto) {
        User currentUser = userService.findCurrentUser();
        Post post = copyDtoToPost(dto);

        LocalDateTime now = LocalDateTime.now();
        post.setCreatedAt(now);
        post.setModifiedAt(now);

        post.setAuthor(currentUser);
        post.setEditor(currentUser);

        if (dto.getTitleImageId() != null) {
            post.setTitleImageId(dto.getTitleImageId());
        }
        if (dto.getCategoryId() != null) {
            post.setCategoryId(dto.getCategoryId());
        }
        post.setStatus(PostStatus.DRAFT);
        adminPostRepository.persist(post);

        addContributors(post, dto.getContributors());
        addTags(post, dto.getTags());

        return post.getId();
    }

    @Override
    // TODO single post cache should evict if dto.status == publish or dto.originalPostStatus == publish
    // TODO homepage cache should evict
//    @CacheEvict
    public void update(AdminPostRequestDto dto) {
        PostStatus status = PostStatus.valueOf(dto.getStatus());
        disableCurrentFeaturePostIfNeeded(status, dto.isSplash(), dto.isFeatured());

        Long postId;
        if (dto.getOriginalId() != null) {
            postId = dto.getOriginalId();
            // delete autosave
            adminPostRepository.deleteById(dto.getId());
        } else {
            postId = dto.getId();
        }

        Post post = adminPostRepository.findOne(postId);
        copyDtoToPost(dto, post);


        if (dto.getTitleImageId() != null) {
            post.setTitleImageId(dto.getTitleImageId());
        }
        if (dto.getCategoryId() != null) {
            post.setCategoryId(dto.getCategoryId());
        }
        addContributors(post, dto.getContributors());
        addTags(post, dto.getTags());

        User currentUser = userService.findCurrentUser();
        post.setEditor(currentUser);
        post.setModifiedAt(LocalDateTime.now());

        adminPostRepository.persist(post);
    }

    @Override
    public void updateAutosave(AdminPostRequestDto dto) {
        User currentUser = userService.findCurrentUser();

        Post post = adminPostRepository.findOne(dto.getId());

        copyDtoToPost(dto, post);

        if (dto.getTitleImageId() != null) {
            post.setTitleImageId(dto.getTitleImageId());
        }
        if (dto.getCategoryId() != null) {
            post.setCategoryId(dto.getCategoryId());
        }
        addContributors(post, dto.getContributors());
        addTags(post, dto.getTags());

        post.setModifiedAt(LocalDateTime.now());
        post.setEditor(currentUser);
        post.setStatus(PostStatus.AUTOSAVE);

        adminPostRepository.persist(post);
    }

    @Override
    public Long deleteById(Long id) {
        adminPostRepository.deleteById(id);
        return id;
    }

    @Override
    public Long delete(Post post) {
        adminPostRepository.delete(post);
        return post.getId();
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
    public Page<AdminPostSummaryDto> findIdsIn(List<Long> ids, Pageable pageable) {
        List<AdminPostSummaryDto> result = adminPostRepository.findByIdIn(ids);
        if (result.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(0, pageable.getPageSize()), 0);
        }
        return new PageImpl<>(result, PageRequest.of(0, pageable.getPageSize()), ids.size());
    }

    @Override
    public Page<AdminPostSummaryDto> findText(String text, PostStatus status, Pageable pageable) {
        User currentUser = userService.findCurrentUser();
        return adminPostRepository.findText(text, currentUser, status, pageable);
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

            disableCurrentFeaturePostIfNeeded(changeTo, post.isSplash(), post.isFeatured());
            // if original post is exists, unlink original
            if (post.getOriginalId() != null) {
                post.setOriginalId(null);
            }
            post.setStatus(changeTo);
            post.setEditorId(currentUser.getId());

            LocalDateTime now = LocalDateTime.now();
            post.setModifiedAt(now);
            adminPostRepository.persist(post);
        }
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                PageRequest.of(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                PageRequest.of(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }

    private void addContributors(Post post, List<String> contributorList) {
        if (contributorList != null) {
            Page<Contributor> page =
                    contributorRepository.findByFullNameIn(contributorList, PageRequest.of(0, 100));
            List<Contributor> contributors = page.getContent();
            addContributorsToPost(post, contributors);
        }
    }

    private void addTags(Post post, List<String> tagList) {
        if (tagList != null) {
            List<Tag> tags = tagRepository.findByNameIn(tagList);
            List<String> tagNames = tags.stream().map(t -> t.getName()).collect(Collectors.toList());
            List<String> subList = (List<String>) CollectionUtils.subtract(
                    tagList, tagNames
            );
            if (!subList.isEmpty()) {
                for (String name : subList) {
                    tagRepository.save(new Tag(name));
                }
                tags = tagRepository.findByNameIn(tagList);
            }
            addTagsToPost(post, tags);
        }

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

        responseDto.setCreatedAt(localDateTimeToInstant(post.getCreatedAt()));
        responseDto.setModifiedAt(localDateTimeToInstant(post.getModifiedAt()));
        responseDto.setPublishedAt(localDateTimeToInstant(post.getPublishedAt()));

        responseDto.setCategoryId(post.getCategoryId());
        responseDto.setAuthorId(post.getAuthorId());
        responseDto.setEditorId(post.getEditorId());

        responseDto.setPicked(post.isPicked());
        responseDto.setFeatured(post.isFeatured());
        responseDto.setSplash(post.isSplash());

        if (post.getAuthor() != null) {
            responseDto.setAuthorName(post.getAuthor().getDisplayName());

        }
        if (post.getEditor() != null) {
            responseDto.setEditorName(post.getEditor().getDisplayName());
        }
        if (post.getCategory() != null) {
            responseDto.setCategoryName(post.getCategory().getName());
        }
        if (post.getOriginal() != null) {
            responseDto.setOriginalId(post.getOriginal().getId());
            responseDto.setOriginalStatus(post.getOriginal().getStatus().toString());
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

    private void disableCurrentFeaturePostIfNeeded(PostStatus status, boolean isSplash, boolean isFeatured) {
        if (status.equals(PostStatus.PUBLISH)) {
            if (isSplash) {
                adminPostRepository.disableSplashPosts();
            }
            if (isFeatured) {
                adminPostRepository.disableFeaturedPosts();
            }
        }
    }
}
