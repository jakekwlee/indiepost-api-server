package com.indiepost.controller.api;

import com.indiepost.dto.request.PostQuery;
import com.indiepost.model.Post;
import com.indiepost.service.AdminPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jake on 7/31/16.
 */
@RestController
@RequestMapping("/api/posts")
public class PostListController {

    private AdminPostService adminPostService;

    @Autowired
    public PostListController(AdminPostService adminPostService) {
        this.adminPostService = adminPostService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Post> getPosts(@RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "30") int maxResults,
                               @RequestParam(required = false, defaultValue = "true") boolean isDesc) {
        return adminPostService.find(page, maxResults, isDesc);
    }

    @RequestMapping(method = RequestMethod.POST)
    public List<Post> getPosts(@RequestBody PostQuery query,
                               @RequestParam(required = false, defaultValue = "0") int page,
                               @RequestParam(required = false, defaultValue = "30") int maxResults,
                               @RequestParam(required = false, defaultValue = "true") boolean isDesc) {
        return adminPostService.find(query, page, maxResults, isDesc);
    }
}
