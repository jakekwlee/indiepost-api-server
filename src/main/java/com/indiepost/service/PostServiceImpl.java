package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.dto.FullTextSearchQuery;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.dto.post.RelatedPostResponseDto;
import com.indiepost.dto.stat.PostStatDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.TagRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
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

/**
 * Created by jake on 7/30/16.
 */
@Service
@Transactional(readOnly = true)
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final StatRepository statRepository;

    private final PostEsRepository postEsRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ImageRepository imageRepository,
                           TagRepository tagRepository,
                           StatRepository statRepository, PostEsRepository postEsRepository) {
        this.postRepository = postRepository;
        this.postEsRepository = postEsRepository;
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
        PostDto dto = postToPostDto(post);
        if (!post.getTags().isEmpty()) {
            List<String> tags = post.getTags().stream()
                    .map(t -> t.getName())
                    .collect(Collectors.toList());
            dto.setTags(tags);
        }
        if (!post.getContributors().isEmpty()) {
            List<ContributorDto> contributors = post.getContributors().stream()
                    .map(c -> {
                        ContributorDto contributorDto = new ContributorDto();
                        BeanUtils.copyProperties(c, contributorDto);
                        return contributorDto;
                    })
                    .collect(Collectors.toList());
            dto.setContributors(contributors);
        }
        return dto;
    }

    @Override
    public Long count() {
        return postRepository.count();
    }

    @Override
    public Long count(PostQuery search) {
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
    public List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Long limit) {
        List<PostStatDto> topStats = statRepository.getPostStatsOrderByPageviews(since, until, limit);
        if (topStats == null || topStats.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> topPostIds = topStats.stream()
                .map(postStat -> postStat.getId())
                .collect(Collectors.toList());

        return postRepository.findByIds(topPostIds);
    }

    @Override
    public List<PostSummaryDto> findScheduledPosts() {
        return postRepository.findScheduledPosts();
    }

    @Override
    public List<PostSummaryDto> search(PostQuery query, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return postRepository.search(query, pageable);
    }

    @Override
    public List<PostSummaryDto> fullTextSearch(FullTextSearchQuery query) {
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
        List<PostSummaryDto> posts = postRepository.findByIds(ids);

        int index = 0;
        for (PostSummaryDto dto : posts) {
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
    public List<PostSummaryDto> findPickedPosts() {
        PostQuery query = new PostQuery();
        query.setPicked(true);
        return search(query, 0, 8, true);
    }

    @Override
    public PostSummaryDto findSplashPost() {
        PostQuery query = new PostQuery();
        query.setSplash(true);
        List<PostSummaryDto> posts = search(query, 0, 1, true);
        return posts.isEmpty() ? null : posts.get(0);
    }

    @Override
    public PostSummaryDto findFeaturePost() {
        PostQuery query = new PostQuery();
        query.setFeatured(true);
        List<PostSummaryDto> posts = search(query, 0, 1, true);
        return posts.isEmpty() ? null : posts.get(0);
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
        dto.setCategoryId(post.getCategoryId());
        dto.setCategoryName(post.getCategory().getName());
        dto.setDisplayName(post.getDisplayName());
        dto.setLikesCount(post.getLikesCount());
        dto.setPublishedAt(post.getPublishedAt());
        dto.setSplash(post.isSplash());
        dto.setFeatured(post.isFeatured());
        dto.setPicked(post.isPicked());
        if (post.getCategory() != null) {
            dto.setCategoryName(post.getCategory().getSlug());
        }
        if (post.getTitleImageId() != null) {
            dto.setTitleImage(post.getTitleImage());
        }
        return dto;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "publishedAt") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "publishedAt");
    }
}
