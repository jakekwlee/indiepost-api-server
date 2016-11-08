package com.indiepost.controller.api.cms;

import com.indiepost.exception.FileSaveException;
import com.indiepost.model.ImageSet;
import com.indiepost.service.ImageService;
import com.indiepost.service.ManagementService;
import com.indiepost.service.PostExcerptService;
import com.indiepost.viewModel.ImageResponse;
import com.indiepost.viewModel.cms.MetaInformation;
import com.indiepost.viewModel.cms.PostMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping("/api/cms")
public class CmsRestController {

    private ManagementService managementService;

    private PostExcerptService postExcerptService;

    private ImageService imageService;

    @Autowired
    public CmsRestController(ManagementService managementService, ImageService imageService, PostExcerptService postExcerptService) {
        this.postExcerptService = postExcerptService;
        this.managementService = managementService;
        this.imageService = imageService;
    }

    @RequestMapping(value = "/metadata", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public MetaInformation getMetadata() {
        return managementService.getMetaInformation();
    }

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public List<PostMeta> getPostMetaList() {
        return managementService.getAllPostsMeta(0, 1000000, true);
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET, produces = {"application/json; charset=UTF-8"})
    public List<ImageSet> getImages() {
        return imageService.findAll(0, 500);
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<ImageResponse> handleImageUpload(
            HttpServletRequest request,
            @RequestParam("files") MultipartFile[] multipartFiles) throws FileSaveException {

        ImageResponse imageResponse = imageService.saveUploadedImage(multipartFiles);
        return ResponseEntity.ok(imageResponse);
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.DELETE, produces = {"application/json; charset=UTF-8"})
    public String handleImageDelete(@PathVariable Long id) throws IOException {
        return imageService.deleteById(id).toString();
    }
}
