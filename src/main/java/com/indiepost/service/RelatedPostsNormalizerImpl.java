package com.indiepost.service;

import com.indiepost.dto.post.RelatedPostsMatchingResult;
import com.indiepost.model.Post;
import com.indiepost.repository.AdminPostRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

import static com.indiepost.utils.DomUtil.getRelatedPostIdsFromPostContent;

@Service
@Transactional
public class RelatedPostsNormalizerImpl implements RelatedPostsNormalizer {

    private final AdminPostRepository repository;

    @Inject
    public RelatedPostsNormalizerImpl(AdminPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public void normalize() {
        List<Post> posts = repository.findAll();

        for (Post post : posts) {
            String content = post.getContent();
            RelatedPostsMatchingResult result = getRelatedPostIdsFromPostContent(content);
            if (result == null || result.getIds().size() == 0) {
                continue;
            }
            post.setContent(result.getContent());
            List<Long> relatedIds = result.getIds();
            List<Post> relatedPostList = repository.findByIds(relatedIds);
            int i = 0;
            for (Post relatedPost : relatedPostList) {
                post.addRelatedPost(relatedPost, i++);
            }
        }
    }
}
