package com.indiepost.controller.api.admin;

import com.indiepost.model.ImageSet;
import com.indiepost.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = "/api/admin/images", produces = {"application/json; charset=UTF-8"})
public class ImageController {

    @Autowired
    private ImageService imageService;


    @RequestMapping(method = RequestMethod.GET)
    public List<ImageSet> getImages(@RequestParam int page, @RequestParam int max) {
        return imageService.findAll(page, max);
    }

    @RequestMapping(method = RequestMethod.POST)
    public List<ImageSet> handleImageUpload(@RequestParam("files") MultipartFile[] multipartFiles) throws IOException {
        return imageService.saveUploadedImages(multipartFiles);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Long handleImageDelete(@PathVariable Long id) throws IOException {
        imageService.deleteById(id);
        return id;
    }
}
