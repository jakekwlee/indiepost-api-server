package com.indiepost.service;

import com.indiepost.dto.analytics.PostStatDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostResponse;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.TagRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.mapper.PostMapper.postToPostDto;
import static com.indiepost.mapper.PostMapper.postToPostSummaryDto;

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final ImageRepository imageRepository;

    private final TagRepository tagRepository;

    private final StatRepository statRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ImageRepository imageRepository,
                           TagRepository tagRepository,
                           StatRepository statRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.tagRepository = tagRepository;
        this.statRepository = statRepository;
    }

    @Override
    public PostDto findOne(Long id) {
        Post post = postRepository.findById(id);
        // TODO I will find better solution!
        post.getTags();
        post.getProfiles();
        ImageSet titleImage = post.getTitleImage();
        if (titleImage != null) {
            titleImage.getOptimized();
        }
        return postToPostDto(post);
    }

    @Override
    public PostDto findOneByLegacyId(Long id) {
        Post post = postRepository.findByLegacyId(id);
        // TODO same above
        post.getTags();
        post.getProfiles();
        ImageSet titleImage = post.getTitleImage();
        if (titleImage != null) {
            titleImage.getOptimized();
        }
        return postToPostDto(post);
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
    public List<PostSummaryDto> find(int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        List<PostSummaryDto> result = postRepository.findByStatus(PostStatus.PUBLISH, pageable);
        return setTitleImages(result);
    }

    @Override
    public List<PostSummaryDto> findByQuery(PostQuery query, boolean isDesc) {
        Pageable pageable = getPageable(query.getPage(), query.getMaxResults(), isDesc);
        List<PostSummaryDto> result = postRepository.findByQuery(query, pageable);
        return setTitleImages(result);
    }

    @Override
    public List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        List<PostSummaryDto> result = postRepository.findByCategoryId(categoryId, pageable);
        return setTitleImages(result);
    }

    @Override
    public List<PostSummaryDto> findByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if (tag == null) {
            return null;
        }
        List<Post> posts = tag.getPosts();
        if (posts == null || posts.size() == 0) {
            return null;
        }
        // TODO find better solution!
        List<PostSummaryDto> dtoList = posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISH))
                .map(post -> postToPostSummaryDto(post))
                .collect(Collectors.toList());
        return setTitleImages(dtoList);
    }

    @Override
    public List<RelatedPostResponse> getRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobile) {
        List<PostSummaryDto> postSummaryDtoList = this.postRepository.findByIds(ids);
        if (postSummaryDtoList == null) {
            return null;
        }

        this.setTitleImages(postSummaryDtoList);
        String legacyPostMobileUrl = "http://www.indiepost.co.kr/indiepost/ContentView.do?no=";
        String legacyPostWebUrl = "http://www.indiepost.co.kr/ContentView.do?no=";

        List<RelatedPostResponse> relatedPostResponseList = new ArrayList<>();
        for (PostSummaryDto postSummaryDto : postSummaryDtoList) {
            RelatedPostResponse relatedPostResponse = new RelatedPostResponse();
            relatedPostResponse.setId(postSummaryDto.getId());
            relatedPostResponse.setTitle(postSummaryDto.getTitle());
            relatedPostResponse.setExcerpt(postSummaryDto.getExcerpt());
            if (postSummaryDto.getTitleImageId() != null) {
                Image image = postSummaryDto.getTitleImage().getThumbnail();
                relatedPostResponse.setImageUrl(image.getFilePath());
                relatedPostResponse.setImageWidth(image.getWidth());
                relatedPostResponse.setImageHeight(image.getHeight());
            }
            if (isLegacy) {
                if (isMobile) {
                    relatedPostResponse.setUrl(legacyPostMobileUrl + postSummaryDto.getLegacyPostId());
                } else {
                    relatedPostResponse.setUrl(legacyPostWebUrl + postSummaryDto.getLegacyPostId());
                }
            } else {
                relatedPostResponse.setUrl("/posts/" + postSummaryDto.getId());
            }
            relatedPostResponseList.add(relatedPostResponse);
        }
        return relatedPostResponseList;
    }

    @Override
    public List<PostSummaryDto> getTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        List<PostStatDto> topStats = statRepository.getPostStatsOrderByPageviews(since, until, limit);
        List<Long> topPostIds = topStats.stream()
                .map(postStat -> postStat.getId())
                .collect(Collectors.toList());

        List<PostSummaryDto> topRatedPosts = postRepository.findByIds(topPostIds);
        if (topRatedPosts == null) {
            return new ArrayList<>();
        }
        return setTitleImages(topRatedPosts);
    }

    @Override
    public List<PostSummaryDto> getScheduledPosts() {
        return postRepository.findScheduledPosts();
    }

    @Override
    public List<PostSummaryDto> search(String text, int page, int maxResults) {
        if (text.length() > 30) {
            text = text.substring(0, 30);
        }
        Pageable pageable = getPageable(page, maxResults, true);
        List<Post> postList = postRepository.search(text, pageable);
        return postList.stream()
                .map(post -> {
                    PostSummaryDto dto = new PostSummaryDto();
                    BeanUtils.copyProperties(post, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Long findIdByLegacyId(Long legacyId) {
        return postRepository.findIdByLegacyId(legacyId);
    }

    @Override
    public PostSummaryDto findSplashPost() {
        PostQuery query = new PostQuery();
        query.setSplash(true);
        query.setPage(0);
        query.setMaxResults(1);
        List<PostSummaryDto> posts = findByQuery(query, true);
        return posts == null ? null : posts.get(0);
    }

    @Override
    public PostSummaryDto findFeaturePost() {
        PostQuery query = new PostQuery();
        query.setFeatured(true);
        query.setPage(0);
        query.setMaxResults(1);
        List<PostSummaryDto> posts = findByQuery(query, true);
        return posts == null ? null : posts.get(0);
    }

    @Override
    public List<PostSummaryDto> findPickedPosts() {
        PostQuery query = new PostQuery();
        query.setPicked(true);
        query.setPage(0);
        query.setMaxResults(8);
        return findByQuery(query, true);
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
