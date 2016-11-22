package com.indiepost.controller.api.admin;

import com.indiepost.model.Post;
import com.indiepost.requestModel.admin.PostRequest;
import com.indiepost.responseModel.admin.PostMeta;
import com.indiepost.responseModel.admin.post.PostResponse;
import com.indiepost.service.ManagementService;
import com.indiepost.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping("/api/admin/posts")
public class PostController {

    @Autowired
    private ManagementService managementService;

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public List<PostMeta> getPostMetaList() {
        return managementService.getAllPostsMeta(0, 1000000, true);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public PostResponse getPost(@PathVariable Long id) {
        return postService.getPostResponse(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = {"application/json; charset=UTF-8"})
    public PostResponse savePost(@RequestBody PostRequest postRequest, @PathVariable Long id) {
        return postService.save(id, postRequest);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json; charset=UTF-8"})
    public Long deletePost(@PathVariable Long id) {
        Post post = postService.findById(id);
        return id;
    }

    @RequestMapping(value = "/draft", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public Long saveDraft(@RequestBody PostRequest postRequest) {
        return postService.saveDraft(postRequest);
    }
}
