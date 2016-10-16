package com.indiepost.controller.api;

import com.indiepost.exception.FileSaveException;
import com.indiepost.service.ImageService;
import com.indiepost.viewModel.ImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by jake on 8/14/16.
 */
@RestController
@RequestMapping("/api/images")
public class ImageRestController {

    @Autowired
    private ImageService imageService;

    public static final String ROOT = "/data/uploads";
    private static final Logger log = LoggerFactory.getLogger(ImageRestController.class);

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<ImageResponse> handleImageUpload(
            HttpServletRequest request,
            @RequestParam("files") MultipartFile[] multipartFiles) throws FileSaveException {

        ImageResponse imageResponse = imageService.saveUploadedImage(multipartFiles);
        return ResponseEntity.ok(imageResponse);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {"application/json; charset=UTF-8"})
    public String handleImageDelete(@PathVariable int id) throws IOException {
        return imageService.deleteById(id).toString();
    }

    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public String handleVideoUpload() {

        return null;
    }
}
