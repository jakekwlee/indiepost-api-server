package com.indiepost.controller.api.admin;

import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.service.ImageService;
import com.indiepost.service.ManagementService;
import com.indiepost.service.PostExcerptService;
import com.indiepost.service.PostService;
import com.indiepost.viewModel.admin.MetaInformation;
import com.indiepost.viewModel.admin.PostMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminRestController {

    // TODO: 11/19/16 Seperate method

    @Autowired
    private ManagementService managementService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostExcerptService postExcerptService;

    @Autowired
    private ImageService imageService;

    @RequestMapping(value = "/metadata", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public MetaInformation getMetadata() {
        return managementService.getMetaInformation();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public List<PostMeta> getPostMetaList() {
        return managementService.getAllPostsMeta(0, 1000000, true);
    }

    @RequestMapping(value = "/post/{id}", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public Post getPost(@PathVariable Long id) {
        return postService.findById(id);
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public List<ImageSet> getImages(@RequestParam int page, @RequestParam int max) {
        return imageService.findAll(page, max);
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public List<ImageSet> handleImageUpload(@RequestParam("files") MultipartFile[] multipartFiles) throws IOException {
        return imageService.saveUploadedImages(multipartFiles);
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE, produces = {"application/json; charset=UTF-8"})
    public Long handleImageDelete(@PathVariable Long id) throws IOException {
        imageService.deleteById(id);
        return id;
    }
}
