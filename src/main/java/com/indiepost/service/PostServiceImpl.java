package com.indiepost.service;

import com.indiepost.dto.ContributorDto;
import com.indiepost.dto.FullTextSearchQuery;
import com.indiepost.dto.Highlight;
import com.indiepost.dto.analytics.PostStatDto;
import com.indiepost.dto.post.PostDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.PostRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
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

    @Inject
    public PostServiceImpl(PostRepository postRepository,
                           StatRepository statRepository,
                           PostEsRepository postEsRepository) {
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
    public Page<PostSummaryDto> find(Pageable pageable) {
        return postRepository.findByStatus(PostStatus.PUBLISH, getPageable(pageable));
    }

    @Override
    public Page<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable) {
        return postRepository.findByCategorySlug(slug, getPageable(pageable));
    }

    @Override
    public Page<PostSummaryDto> findByTagName(String tagName, Pageable pageable) {
        return postRepository.findByTagName(tagName, getPageable(pageable));
    }

    @Override
    public Page<PostSummaryDto> findByContributorFullName(String fullName, Pageable pageable) {
        return postRepository.findByContributorFullName(fullName, getPageable(pageable));
    }

    @Override
    public Page<PostSummaryDto> findReadingHistoryByUserId(Long userId, Pageable pageable) {
        return postRepository.findReadingHistoryByUserId(userId, getPageable(pageable));
    }

    @Override
    public Page<PostSummaryDto> findBookmarksByUserId(Long userId, Pageable pageable) {
        return postRepository.findBookmarksByUserId(userId, getPageable(pageable));
    }

    @Override
    public List<PostSummaryDto> findTopRatedPosts(LocalDateTime since, LocalDateTime until, Integer limit) {
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
    public Page<PostSummaryDto> query(PostQuery postQuery, int page, int size) {
        return postRepository.query(postQuery, getPageable(page, size));
    }

    @Override
    public Page<PostSummaryDto> fullTextSearch(FullTextSearchQuery query) {
        String text = query.getText();
        if (text.length() > 30) {
            text = text.substring(0, 30);
        }
        Pageable pageable = getPageable(PageRequest.of(query.getPage(), query.getMaxResults()));
        List<PostEs> postEsList = postEsRepository.search(text, PostStatus.PUBLISH, pageable);
        if (postEsList.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Long total = postEsRepository.count(text, PostStatus.PUBLISH).longValue();
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
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public List<PostSummaryDto> findPickedPosts() {
        PostQuery query = new PostQuery();
        query.setPicked(true);
        return query(query, 0, 8).getContent();
    }

    @Override
    public PostSummaryDto findSplashPost() {
        PostQuery query = new PostQuery();
        query.setSplash(true);
        List<PostSummaryDto> posts = query(query, 0, 1).getContent();
        return posts.isEmpty() ? null : posts.get(0);
    }

    @Override
    public PostSummaryDto findFeaturePost() {
        PostQuery query = new PostQuery();
        query.setFeatured(true);
        List<PostSummaryDto> posts = query(query, 0, 1).getContent();
        return posts.isEmpty() ? null : posts.get(0);
    }

    private Pageable getPageable(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.Direction.DESC,
                "publishedAt"
        );
    }

    private Pageable getPageable(int page, int size) {
        return PageRequest.of(
                page,
                size,
                Sort.Direction.DESC,
                "publishedAt"
        );
    }
}
