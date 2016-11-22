package com.indiepost.controller.api.admin;

import com.indiepost.model.Post;
import com.indiepost.requestModel.admin.PostRequest;
import com.indiepost.responseModel.admin.PostResponse;
import com.indiepost.responseModel.admin.SimplifiedPost;
import com.indiepost.service.AdminService;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = "/api/admin/posts", produces = {"application/json; charset=UTF-8"})
public class PostController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.GET)
    public List<SimplifiedPost> getPostMetaList() {
        return adminService.getAllPostsMeta(0, 1000000, true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPostResponse(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public PostResponse savePost(@RequestBody PostRequest postRequest, @PathVariable Long id) {
        return postService.update(id, postRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long deletePost(@PathVariable Long id) {
        Post post = postService.findById(id);
        postService.delete(post);
        return id;
    }

    @RequestMapping(value = "/draft", method = RequestMethod.POST)
    public Long saveDraft(@RequestBody PostRequest postRequest) {
        return postService.saveDraft(postRequest);
    }
}
