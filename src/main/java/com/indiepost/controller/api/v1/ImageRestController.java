package com.indiepost.controller.api.v1;

import com.indiepost.exception.FileSaveException;
import com.indiepost.service.ImageService;
import com.indiepost.viewModel.ImageUploadForm;
import com.indiepost.viewModel.ImageMetaInformation;
import com.indiepost.viewModel.ImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;

/**
 * Created by jake on 8/14/16.
 */
@RestController
@RequestMapping("/api/v1/images")
public class ImageRestController {

    @Autowired
    private ImageService imageService;

    public static final String ROOT = "/data/uploads";
    private static final Logger log = LoggerFactory.getLogger(ImageRestController.class);

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<ImageResponse> handleImageUpload(
            HttpServletRequest request,
            @RequestParam("files") MultipartFile[] multipartFiles) throws FileSaveException {
        HttpServletRequest r = request;

        ImageResponse imageResponse = imageService.saveUploadedImage(multipartFiles);
        return ResponseEntity.ok(imageResponse);
    }

    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public String handleVideoUpload() {

        return null;
    }
}
