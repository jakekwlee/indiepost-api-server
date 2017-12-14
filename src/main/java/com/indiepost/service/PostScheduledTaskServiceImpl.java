package com.indiepost.service;

import com.indiepost.enums.Types;
import com.indiepost.model.Metadata;
import com.indiepost.model.Post;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.MetadataRepository;
import com.indiepost.repository.StatRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.mapper.PostMapper.toPostEs;

@Service
@Transactional
public class PostScheduledTaskServiceImpl implements PostScheduledTaskService {

    private static final Logger log = LoggerFactory.getLogger(AdminPostServiceImpl.class);

    private final AdminPostRepository adminPostRepository;

    private final MetadataRepository metadataRepository;

    private final PostEsRepository postEsRepository;

    private final StatRepository statRepository;

    @Inject
    public PostScheduledTaskServiceImpl(AdminPostRepository adminPostRepository, MetadataRepository metadataRepository, PostEsRepository postEsRepository, StatRepository statRepository) {
        this.adminPostRepository = adminPostRepository;
        this.metadataRepository = metadataRepository;
        this.postEsRepository = postEsRepository;
        this.statRepository = statRepository;
    }

    @Override
    public void publishScheduledPosts() {
        List<Post> posts = adminPostRepository.findScheduledToBePublished();
        if (posts == null) {
            return;
        }
        for (Post post : posts) {
            if (post.isSplash()) {
                adminPostRepository.disableSplashPosts();
            }
            if (post.isFeatured()) {
                adminPostRepository.disableFeaturedPosts();
            }
            post.setStatus(Types.PostStatus.PUBLISH);
            log.info(String.format("[%s] %s", post.getId(), post.getTitle()));
        }
    }

    @Override
    public void rebuildElasticsearchIndices() {
        LocalDateTime indicesLastUpdated = metadataRepository.findOne(1L)
                .getSearchIndexLastUpdated();
        if (indicesLastUpdated == null) {
            indicesLastUpdated = LocalDateTime.MIN;
        }
//        postEsRepository.deleteIndex();
        List<Post> posts = adminPostRepository.findScheduledToBeIndexed(indicesLastUpdated);
        if (posts.size() == 0) {
            return;
        }
        List<PostEs> postEsList = posts.stream()
                .map(post -> toPostEs(post))
                .collect(Collectors.toList());
//        postEsRepository.save(postEsList);
        log.info(String.format("%d posts are indexed", posts.size()));
    }

    @Override
    public void updateCachedPostStats() {
        statRepository.deleteAllPostStatsCache();
        statRepository.updatePostStatsCache();

        Metadata metadata = metadataRepository.findOne(1L);
        if (metadata == null) {
            metadata = new Metadata();
        }
        LocalDateTime now = LocalDateTime.now();
        metadata.setPostStatsLastUpdated(now);
        metadataRepository.save(metadata);
    }
}
