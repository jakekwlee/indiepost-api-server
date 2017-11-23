package com.indiepost.service;

import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.analytics.PostStatDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostSearch;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostResponse;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
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

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final StatRepository statRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, StatRepository statRepository) {
        this.postRepository = postRepository;
        this.statRepository = statRepository;
    }

    @Override
    public PostDto findOne(Long id) {
        Post post = postRepository.findById(id);
        // TODO I will find better solution!
        post.getTags();
        post.getContributors();
        ImageSet titleImage = post.getTitleImage();
        if (titleImage != null) {
            titleImage.getImages();
        }
        return postToPostDto(post);
    }

    @Override
    public PostDto findOneByLegacyId(Long id) {
        Post post = postRepository.findByLegacyId(id);
        // TODO same above
        post.getTags();
        post.getContributors();
        ImageSet titleImage = post.getTitleImage();
        if (titleImage != null) {
            titleImage.getImages();
        }
        return postToPostDto(post);
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long count(PostSearch search) {
        return postRepository.count(search);
    }

    @Override
    public List<PostSummaryDto> findByIds(List<Long> ids) {
        return postRepository.findByIds(ids);
    }

    @Override
    public List<PostSummaryDto> find(int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postRepository.findByStatus(PostStatus.PUBLISH, pageable);
    }

    @Override
    public List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public List<PostSummaryDto> findByTagName(String tagName, int page, int maxResults, boolean isDesc) {
        return postRepository.findByTagName(tagName, new PageRequest(page, maxResults));
    }

    @Override
    public List<RelatedPostResponse> findRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobileApp) {
        List<PostSummaryDto> posts = this.postRepository.findByIds(ids);

        if (posts == null) {
            return null;
        }

        String legacyPostMobileUrl = "http://www.indiepost.co.kr/indiepost/ContentView.do?no=";
        String legacyPostWebUrl = "http://www.indiepost.co.kr/ContentView.do?no=";

        List<RelatedPostResponse> relatedPostResponseList = new ArrayList<>();

        for (PostSummaryDto post : posts) {
            RelatedPostResponse relatedPostResponse = new RelatedPostResponse();
            relatedPostResponse.setId(post.getId());
            relatedPostResponse.setTitle(post.getTitle());
            relatedPostResponse.setExcerpt(post.getExcerpt());
            if (post.getTitleImage() != null) {
                relatedPostResponse.setImageUrl(post.getTitleImage().getThumbnail());
            }
            if (isLegacy) {
                if (isMobileApp) {
                    relatedPostResponse.setUrl(legacyPostMobileUrl + post.getLegacyPostId());
                } else {
                    relatedPostResponse.setUrl(legacyPostWebUrl + post.getLegacyPostId());
                }
            } else {
                relatedPostResponse.setUrl("/posts/" + post.getId());
            }
            relatedPostResponseList.add(relatedPostResponse);
        }
        return relatedPostResponseList;
    }

    @Override
    public List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        List<PostStatDto> topStats = statRepository.getPostStatsOrderByPageviews(since, until, limit);
        List<Long> topPostIds = topStats.stream()
                .map(postStat -> postStat.getId())
                .collect(Collectors.toList());

        List<PostSummaryDto> topRatedPosts = postRepository.findByIds(topPostIds);
        if (topRatedPosts == null) {
            return new ArrayList<>();
        }
        return topRatedPosts;
    }

    @Override
    public List<PostSummaryDto> findScheduledPosts() {
        return postRepository.findScheduledPosts();
    }

    @Override
    public List<PostSummaryDto> search(String text, int page, int maxResults) {
        if (text.length() > 30) {
            text = text.substring(0, 30);
        }
        Pageable pageable = getPageable(page, maxResults, true);
        List<Post> posts = postRepository.search(text, pageable);
        if (posts == null) {
            return new ArrayList<>();
        }
        return toPostSummaryDtoList(posts);
    }

    @Override
    public List<PostSummaryDto> search(PostSearch query, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postRepository.search(query, pageable);
    }

    @Override
    public Long findIdByLegacyId(Long legacyId) {
        return postRepository.findIdByLegacyId(legacyId);
    }

    @Override
    public List<PostSummaryDto> findPickedPosts() {
        PostSearch query = new PostSearch();
        query.setPicked(true);
        return search(query, 0, 8, true);
    }

    @Override
    public PostSummaryDto findSplashPost() {
        PostSearch query = new PostSearch();
        query.setSplash(true);
        List<PostSummaryDto> posts = search(query, 0, 1, true);
        return posts == null ? null : posts.get(0);
    }

    @Override
    public PostSummaryDto findFeaturePost() {
        PostSearch query = new PostSearch();
        query.setFeatured(true);
        List<PostSummaryDto> posts = search(query, 0, 1, true);
        return posts == null ? null : posts.get(0);
    }

    private List<PostSummaryDto> toPostSummaryDtoList(List<Post> posts) {
        return posts.stream()
                .map(post -> toPostSummaryDto(post))
                .collect(Collectors.toList());
    }

    private PostSummaryDto toPostSummaryDto(Post post) {
        PostSummaryDto dto = new PostSummaryDto();
        dto.setId(post.getId());
        dto.setLegacyPostId(post.getLegacyPostId());
        dto.setTitle(post.getTitle());
        dto.setBylineName(post.getBylineName());
        dto.setBookmarkCount(post.getBookmarkCount());
        dto.setPublishedAt(post.getPublishedAt());
        dto.setSplash(post.isSplash());
        dto.setFeatured(post.isFeatured());
        dto.setPicked(post.isPicked());
        if (post.getCategory() != null) {
            dto.setCategory(post.getCategory().getSlug());
        }
        ImageSet titleImage = post.getTitleImage();
        if (titleImage != null) {
            ImageSetDto titleImageDto = new ImageSetDto();
            titleImageDto.setId(titleImage.getId());
            titleImageDto.setId(titleImage.getId());
            if (titleImage.getOriginal() != null) {
                titleImageDto.setOriginal(titleImage.getOriginal().getFilePath());
            }
            if (titleImage.getLarge() != null) {
                titleImageDto.setLarge(titleImage.getLarge().getFilePath());
            }
            if (titleImage.getOptimized() != null) {
                titleImageDto.setOptimized(titleImage.getOptimized().getFilePath());
            }
            if (titleImage.getSmall() != null) {
                titleImageDto.setSmall(titleImage.getSmall().getFilePath());
            }
            if (titleImage.getThumbnail() != null) {
                titleImageDto.setThumbnail(titleImage.getThumbnail().getFilePath());
            }
        }
        return dto;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
