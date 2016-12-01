package com.indiepost.controller.api.admin;

import com.indiepost.model.Post;
import com.indiepost.requestModel.admin.PostRequest;
import com.indiepost.responseModel.admin.PostResponse;
import com.indiepost.responseModel.admin.SimplifiedPost;
import com.indiepost.service.AdminService;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @RequestMapping(value= "/autosave", method=RequestMethod.POST)
    public PostResponse createAutosave(@RequestBody PostRequest postRequest) {
        return postService.createAutosave(postRequest);
    }

    @RequestMapping(value= "/autosave", method=RequestMethod.PUT)
    public PostResponse updateAutosave(@RequestBody PostRequest postRequest) {
        return postService.createAutosave(postRequest);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SimplifiedPost> getPostList() {
        return adminService.getAllSimplifiedPosts(0, 1000000, true);
    }

    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET)
    public List<SimplifiedPost> getLastUpdated() {
        return adminService.getLastUpdated(getYesterday());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPostResponse(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public List<SimplifiedPost> updatePost(@RequestBody PostRequest postRequest, @PathVariable Long id) {
        postService.update(id, postRequest);
        return adminService.getLastUpdated(getYesterday());
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long deletePost(@PathVariable Long id) {
        Post post = postService.findById(id);
        postService.delete(post);
        return id;
    }

    @RequestMapping(value = "/draft", method = RequestMethod.POST)
    public PostResponse createDraft(@RequestBody PostRequest postRequest) {
        return postService.createAutosave(postRequest);
    }

    @RequestMapping(value = "/draft/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateDraft(@RequestBody PostRequest postRequest, @PathVariable Long id) {
        postService.updateAutosave(id, postRequest);
    }

    private Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date oneDayBefore = cal.getTime();
        return oneDayBefore;
    }
}
