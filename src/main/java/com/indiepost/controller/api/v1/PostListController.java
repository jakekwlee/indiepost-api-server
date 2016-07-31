package com.indiepost.controller.api.v1;

import com.indiepost.model.Post;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/api/v1")
public class PostListController {

    @Autowired
    private PostService postService;

    @RequestMapping("/posts")
    public List<Post> getPosts() {
        return postService.findAllForUser(0, 10);
    }
}
