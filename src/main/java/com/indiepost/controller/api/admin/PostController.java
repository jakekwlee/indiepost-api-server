package com.indiepost.controller.api.admin;

import com.indiepost.model.request.AdminPostRequest;
import com.indiepost.model.response.AdminPostResponse;
import com.indiepost.model.response.SimplifiedPost;
import com.indiepost.service.AdminService;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AdminPostResponse getPost(@PathVariable Long id) {
        return postService.getPostResponse(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public AdminPostResponse savePost(@RequestBody AdminPostRequest adminPostRequest) {
        return postService.save(adminPostRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updatePost(@RequestBody AdminPostRequest adminPostRequest, @PathVariable Long id) {
        postService.update(id, adminPostRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long deletePost(@PathVariable Long id) {
        postService.deleteById(id);
        return id;
    }

    @RequestMapping(value = "/autosave", method = RequestMethod.POST)
    public AdminPostResponse createAutosave(@RequestBody AdminPostRequest adminPostRequest) {
        return postService.createAutosave(adminPostRequest);
    }

    @RequestMapping(value = "/autosave/{id}", method = RequestMethod.PUT)
    public void updateAutosave(@PathVariable Long id, @RequestBody AdminPostRequest adminPostRequest) {
        postService.updateAutosave(id, adminPostRequest);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<SimplifiedPost> getPostList() {
        return adminService.getAllSimplifiedPosts(0, 1000000, true);
    }

    @RequestMapping(value = "/lastUpdated", method = RequestMethod.GET)
    public List<SimplifiedPost> getLastUpdated() {
        return adminService.getLastUpdated(getYesterday());
    }


    private Date getYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -1);
        Date oneDayBefore = cal.getTime();
        return oneDayBefore;
    }
}
