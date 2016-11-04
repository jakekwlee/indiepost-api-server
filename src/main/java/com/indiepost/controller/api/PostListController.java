package com.indiepost.controller.api;

import com.indiepost.model.Post;
import com.indiepost.service.PostExcerptService;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/api/posts")
public class PostListController {

    private PostExcerptService postExcerptService;

    private PostService postService;

    @Autowired
    public PostListController(PostService postService, PostExcerptService postExcerptService) {
        this.postService = postService;
        this.postExcerptService = postExcerptService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> getPosts() {
        return postExcerptService.findAll(0, 10, true);
    }
}
