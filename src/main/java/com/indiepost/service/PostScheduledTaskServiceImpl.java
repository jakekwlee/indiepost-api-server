package com.indiepost.service;

import com.indiepost.enums.Types;
import com.indiepost.model.Post;
import com.indiepost.model.elasticsearch.PostEs;
import com.indiepost.repository.AdminPostRepository;
import com.indiepost.repository.MetadataRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.utils.DomUtil.htmlToText;

@Service
@Transactional
public class PostScheduledTaskServiceImpl implements PostScheduledTaskService {

    private static final Logger log = LoggerFactory.getLogger(AdminPostServiceImpl.class);

    private final AdminPostRepository adminPostRepository;

    private final MetadataRepository metadataRepository;

    private final PostEsRepository postEsRepository;

    @Autowired
    public PostScheduledTaskServiceImpl(AdminPostRepository adminPostRepository, MetadataRepository metadataRepository, PostEsRepository postEsRepository) {
        this.adminPostRepository = adminPostRepository;
        this.metadataRepository = metadataRepository;
        this.postEsRepository = postEsRepository;
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
            adminPostRepository.update(post);
            log.info(String.format("[%s] %s", post.getId(), post.getTitle()));
        }
    }

    @Override
    public void updateElasticsearchIndices() {
        LocalDateTime indicesLastUpdated = metadataRepository.findOne(1L)
                .getSearchIndexLastUpdated();
        List<Post> posts = adminPostRepository.findScheduledToBeIndexed(indicesLastUpdated);
        if (posts.size() == 0) {
            return;
        }
        List<PostEs> postEsList = posts.stream()
                .map(post -> toPostEs(post))
                .collect(Collectors.toList());
        postEsRepository.save(postEsList);
        log.info(String.format("%d posts are indexed", posts.size()));
    }

    private PostEs toPostEs(Post post) {
        PostEs postEs = new PostEs();
        postEs.setId(post.getId());
        postEs.setTitle(post.getTitle());
        postEs.setBylineName(post.getDisplayName());
        postEs.setExcerpt(post.getExcerpt());

        List<String> contributors = post.getContributors().stream()
                .map(c -> c.getName())
                .collect(Collectors.toList());
        postEs.setContributors(contributors);

        List<String> tags = post.getTags().stream()
                .map(t -> t.getName())
                .collect(Collectors.toList());
        postEs.setTags(tags);
        postEs.setContent(htmlToText(post.getContent()));
        return postEs;
    }
}
