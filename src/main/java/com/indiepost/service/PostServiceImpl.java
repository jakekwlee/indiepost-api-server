package com.indiepost.service;

import com.indiepost.dto.ImageSetDto;
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
    public Long count(PostQuery query) {
        return postRepository.count(query);
    }

    @Override
    public List<PostSummaryDto> findByIds(List<Long> ids) {
        List<Post> posts = postRepository.findByIds(ids);
        return toPostSummaryDtoList(posts);
    }

    @Override
    public List<PostSummaryDto> find(int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        List<Post> posts = postRepository.findByStatus(PostStatus.PUBLISH, pageable);
        return toPostSummaryDtoList(posts);
    }

    @Override
    public List<PostSummaryDto> findByQuery(PostQuery query, boolean isDesc) {
        Pageable pageable = getPageable(query.getPage(), query.getMaxResults(), isDesc);
        List<Post> posts = postRepository.findByQuery(query, pageable);
        return toPostSummaryDtoList(posts);
    }

    @Override
    public List<PostSummaryDto> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        List<Post> posts = postRepository.findByCategoryId(categoryId, pageable);
        return toPostSummaryDtoList(posts);
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
        return posts.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISH))
                .map(post -> toPostSummaryDto(post))
                .collect(Collectors.toList());
    }

    @Override
    public List<RelatedPostResponse> getRelatedPosts(List<Long> ids, boolean isLegacy, boolean isMobileApp) {
        List<Post> posts = this.postRepository.findByIds(ids);

        if (posts == null) {
            return null;
        }

        String legacyPostMobileUrl = "http://www.indiepost.co.kr/indiepost/ContentView.do?no=";
        String legacyPostWebUrl = "http://www.indiepost.co.kr/ContentView.do?no=";

        List<RelatedPostResponse> relatedPostResponseList = new ArrayList<>();

        for (Post post : posts) {
            RelatedPostResponse relatedPostResponse = new RelatedPostResponse();
            relatedPostResponse.setId(post.getId());
            relatedPostResponse.setTitle(post.getTitle());
            relatedPostResponse.setExcerpt(post.getExcerpt());
            if (post.getTitleImage() != null) {
                Image image = post.getTitleImage().getThumbnail();
                relatedPostResponse.setImageUrl(image.getFilePath());
                relatedPostResponse.setImageWidth(image.getWidth());
                relatedPostResponse.setImageHeight(image.getHeight());
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
    public List<PostSummaryDto> getTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        List<PostStatDto> topStats = statRepository.getPostStatsOrderByPageviews(since, until, limit);
        List<Long> topPostIds = topStats.stream()
                .map(postStat -> postStat.getId())
                .collect(Collectors.toList());

        List<Post> topRatedPosts = postRepository.findByIds(topPostIds);
        if (topRatedPosts == null) {
            return new ArrayList<>();
        }
        return toPostSummaryDtoList(topRatedPosts);
    }

    @Override
    public List<PostSummaryDto> getScheduledPosts() {
        List<Post> posts = postRepository.findScheduledPosts();
        return toPostSummaryDtoList(posts);
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
