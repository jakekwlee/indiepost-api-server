package com.indiepost.service;

import com.indiepost.dto.PostDto;
import com.indiepost.dto.PostQuery;
import com.indiepost.dto.PostSummaryDto;
import com.indiepost.dto.RelatedPostResponseDto;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import com.indiepost.service.mapper.PostMapperService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapperService postMapperService;

    private final ImageRepository imageRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ImageRepository imageRepository,
                           PostMapperService postMapperService) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.postMapperService = postMapperService;
    }

    @Override
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        List<Tag> tagList = post.getTags();
        if (tagList.size() > 0) {
            tagList.get(0);
        }
        post.getTitleImage().getOptimized();
        return postMapperService.postToPostDto(post);
    }

    @Override
    public PostDto findByLegacyId(Long id) {
        Post post = postRepository.findByLegacyId(id);
        List<Tag> tagList = post.getTags();
        if (tagList.size() > 0) {
            tagList.get(0);
        }
        post.getTitleImage().getOptimized();
        return postMapperService.postToPostDto(post);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long count(PostQuery query) {
        return postRepository.count(query);
    }

    @Override
    public List<PostSummaryDto> findByIds(List<Long> ids) {
        List<PostSummaryDto> result = postRepository.findByIds(ids);
        return setTitleImages(result);
    }

    @Override
    public List<PostSummaryDto> findAll(int page, int maxResults, boolean isDesc) {
        List<PostSummaryDto> result = postRepository.findByStatus(PostEnum.Status.PUBLISH, getPageable(page, maxResults, isDesc));
        return setTitleImages(result);
    }

    @Override
    public List<PostSummaryDto> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc) {
        List<PostSummaryDto> result = postRepository.findByQuery(query, getPageable(page, maxResults, isDesc));
        return setTitleImages(result);
    }

    @Override
    public List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc) {
        List<PostSummaryDto> result = postRepository.findByCategoryId(categoryId, getPageable(page, maxResults, isDesc));
        return setTitleImages(result);
    }

    @Override
    public List<RelatedPostResponseDto> getRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile) {
        List<PostSummaryDto> postSummaryDtoList = this.postRepository.findByIds(ids);
        if (postSummaryDtoList == null) {
            return null;
        }

        this.setTitleImages(postSummaryDtoList);
        String legacyPostMobileUrl = "http://www.indiepost.co.kr/indiepost/ContentView.do?no=";
        String legacyPostWebUrl = "http://www.indiepost.co.kr/ContentView.do?no=";

        List<RelatedPostResponseDto> relatedPostResponseDtoList = new ArrayList<>();
        for (PostSummaryDto postSummaryDto : postSummaryDtoList) {
            RelatedPostResponseDto relatedPostResponseDto = new RelatedPostResponseDto();
            relatedPostResponseDto.setId(postSummaryDto.getId());
            relatedPostResponseDto.setTitle(postSummaryDto.getTitle());
            relatedPostResponseDto.setExcerpt(postSummaryDto.getExcerpt());
            if (postSummaryDto.getTitleImageId() != null) {
                Image image = postSummaryDto.getTitleImage().getThumbnail();
                relatedPostResponseDto.setImageUrl(image.getFileUrl());
                relatedPostResponseDto.setImageWidth(image.getWidth());
                relatedPostResponseDto.setImageHeight(image.getHeight());
            }
            if (isLegacy) {
                if (isMobile) {
                    relatedPostResponseDto.setUrl(legacyPostMobileUrl + postSummaryDto.getLegacyPostId());
                } else {
                    relatedPostResponseDto.setUrl(legacyPostWebUrl + postSummaryDto.getLegacyPostId());
                }
            } else {
                relatedPostResponseDto.setUrl("/posts/" + postSummaryDto.getId());
            }
            relatedPostResponseDtoList.add(relatedPostResponseDto);
        }
        return relatedPostResponseDtoList;
    }

    @Override
    public List<PostSummaryDto> search(String text, int page, int maxResults) {
        Pageable pageable = getPageable(page, maxResults, true);
        List<Post> postList = postRepository.search(text, pageable);
        return postList.stream().map(post -> {
            PostSummaryDto dto = new PostSummaryDto();
            BeanUtils.copyProperties(post, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    private List<PostSummaryDto> setTitleImages(List<PostSummaryDto> postSummaryDtoList) {
        List<Long> ids = postSummaryDtoList.stream()
                .filter(postExcerpt -> postExcerpt.getTitleImageId() != null)
                .map(PostSummaryDto::getTitleImageId)
                .collect(Collectors.toList());

        if (ids.size() == 0) {
            return postSummaryDtoList;
        }

        List<ImageSet> imageSetList = imageRepository.findByIds(ids);

        for (PostSummaryDto postSummaryDto : postSummaryDtoList) {
            Long titleImageId = postSummaryDto.getTitleImageId();
            for (ImageSet imageSet : imageSetList) {
                if (imageSet.getId().equals(titleImageId)) {
                    postSummaryDto.setTitleImage(imageSet);
                    break;
                }
            }
        }

        return postSummaryDtoList;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
