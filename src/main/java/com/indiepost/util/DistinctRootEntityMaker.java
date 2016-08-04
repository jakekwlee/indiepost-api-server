package com.indiepost.util;

import com.indiepost.model.MediaContent;
import com.indiepost.model.Post;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by jake on 16. 8. 1.
 */
public class DistinctRootEntityMaker {
    public Post getDistinctEntity(List<Post> posts) {
        Post post = new Post();
        Post p;
        LinkedHashSet<MediaContent> mediaContents = new LinkedHashSet<>();
        for (int i = 0; i < posts.size(); i++) {
            p = posts.get(i);
            if (i == 0) {
                post.setId(p.getId());
                post.setAuthor(p.getAuthor());
                post.setCategory(p.getCategory());
                post.setContent(p.getContent());
                post.setTitle(p.getTitle());
            }
            mediaContents.add(p.getMediaContents().iterator().next());
        }
        post.setMediaContents(mediaContents);
        return post;
    }
}
