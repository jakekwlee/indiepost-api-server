package com.indiepost.service;

import com.indiepost.dto.*;
import com.indiepost.dto.stat.PostStatDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.Tag;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.TagRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import com.indiepost.service.mapper.PostMapperService;
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

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final PostMapperService postMapperService;

    private final ImageRepository imageRepository;

    private final TagRepository tagRepository;

    private final StatRepository statRepository;

    private final PostEsRepository postEsRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ImageRepository imageRepository,
                           PostMapperService postMapperService, TagRepository tagRepository,
                           StatRepository statRepository, PostEsRepository postEsRepository) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.postMapperService = postMapperService;
        this.tagRepository = tagRepository;
        this.statRepository = statRepository;
        this.postEsRepository = postEsRepository;
    }

    @Override
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        List<Tag> tagList = post.getTags();
        if (tagList.size() > 0) {
            tagList.get(0);
        }
        ImageSet titleImage = post.getTitleImage();
        if (titleImage != null) {
            titleImage.getOptimized();
        }
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
    public List<PostSummary> findByIds(List<Long> ids) {
        List<PostSummary> result = postRepository.findByIds(ids);
        return setTitleImages(result);
    }

    @Override
    public List<PostSummary> findAll(int page, int maxResults, boolean isDesc) {
        List<PostSummary> result = postRepository.findByStatus(PostStatus.PUBLISH, getPageable(page, maxResults, isDesc));
        return setTitleImages(result);
    }

    @Override
    public List<PostSummary> findByQuery(PostQuery query, int page, int maxResults, boolean isDesc) {
        List<PostSummary> result = postRepository.findByQuery(query, getPageable(page, maxResults, isDesc));
        return setTitleImages(result);
    }

    @Override
    public List<PostSummary> findByCategoryId(Long categoryId, int page, int maxResults, boolean isDesc) {
        List<PostSummary> result = postRepository.findByCategoryId(categoryId, getPageable(page, maxResults, isDesc));
        return setTitleImages(result);
    }

    @Override
    public List<PostSummary> findByTagName(String tagName) {
        Tag tag = tagRepository.findByTagName(tagName);
        if (tag == null) {
            return null;
        }
        List<Post> postList = tag.getPosts();
        if (postList == null || postList.size() == 0) {
            return null;
        }
        List<PostSummary> dtoList = postList.stream()
                .filter(post -> post.getStatus().equals(PostStatus.PUBLISH))
                .map(postMapperService::postToPostSummaryDto)
                .collect(Collectors.toList());
        return setTitleImages(dtoList);
    }

    @Override
    public List<PostSummary> getTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        List<PostStatDto> topStats = statRepository.getPostStatsOrderByPageviews(since, until, limit);
        List<Long> topPostIds = topStats.stream()
                .map(postStat -> postStat.getId())
                .collect(Collectors.toList());

        List<PostSummary> topRatedPosts = postRepository.findByIds(topPostIds);
        if (topRatedPosts == null) {
            return new ArrayList<>();
        }
        return setTitleImages(topRatedPosts);
    }

    @Override
    public List<PostSummary> getScheduledPosts() {
        return postRepository.findScheduledPosts();
    }

    @Override
    public Long findIdByLegacyId(Long legacyId) {
        return postRepository.findIdByLegacyId(legacyId);
    }

    @Override
    public List<PostSummary> fullTextSearch(FullTextSearchQuery query) {
        String text = query.getText();
        if (text.length() > 30) {
            text = text.substring(0, 30);
        }
        Pageable pageable = getPageable(query.getPage(), query.getMaxResults(), true);
        List<PostEs> postEsList = postEsRepository.search(text, PostStatus.PUBLISH, pageable);
        if (postEsList.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> ids = postEsList.stream()
                .map(p -> p.getId())
                .collect(Collectors.toList());
        List<PostSummary> posts = postRepository.findByIds(ids);

        int index = 0;
        for (PostSummary dto : posts) {
            PostEs postEs = postEsList.get(index);
            String title = postEs.getTitle();
            String excerpt = postEs.getExcerpt();
            if (title == null && excerpt == null) {
                continue;
            }
            Highlight highlight = new Highlight(title, excerpt);
            dto.setHighlight(highlight);
            index = index + 1;
        }
        return posts;
    }

    @Override
    public PostSummary findSplashPost() {
        PostQuery query = new PostQuery();
        query.setSplash(true);
        List<PostSummary> posts = findByQuery(query, 0, 1, true);
        return posts == null ? null : posts.get(0);
    }

    @Override
    public PostSummary findFeaturePost() {
        PostQuery query = new PostQuery();
        query.setFeatured(true);
        List<PostSummary> posts = findByQuery(query, 0, 1, true);
        return posts == null ? null : posts.get(0);
    }

    @Override
    public List<PostSummary> findPickedPosts() {
        PostQuery query = new PostQuery();
        query.setPicked(true);
        return findByQuery(query, 0, 8, true);
    }

    private List<PostSummary> setTitleImages(List<PostSummary> postSummaryList) {
        List<Long> ids = postSummaryList.stream()
                .filter(postExcerpt -> postExcerpt.getTitleImageId() != null)
                .map(PostSummary::getTitleImageId)
                .collect(Collectors.toList());

        if (ids.size() == 0) {
            return postSummaryList;
        }

        List<ImageSet> imageSetList = imageRepository.findByIds(ids);

        for (PostSummary postSummary : postSummaryList) {
            Long titleImageId = postSummary.getTitleImageId();
            for (ImageSet imageSet : imageSetList) {
                if (imageSet.getId().equals(titleImageId)) {
                    postSummary.setTitleImage(imageSet);
                    break;
                }
            }
        }

        return postSummaryList;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
