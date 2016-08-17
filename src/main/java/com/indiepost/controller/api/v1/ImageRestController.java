package com.indiepost.controller.api.v1;

import com.indiepost.viewModel.ImageUploadForm;
import com.indiepost.viewModel.ImageMetaInformation;
import com.indiepost.viewModel.ImageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    public static final String ROOT = "/data/uploads";
    private static final Logger log = LoggerFactory.getLogger(ImageRestController.class);

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json; charset=UTF-8"})
    public ResponseEntity<ImageResponse> handleImageUpload(@Valid ImageUploadForm imageUploadForm, BindingResult bindingResult) {
        ImageMetaInformation imageMetaInformation = new ImageMetaInformation();
        imageMetaInformation.setId(123);
        imageMetaInformation.setDeleteUrl("/api/v1/images");
        imageMetaInformation.setDeleteType(RequestMethod.DELETE.toString());
        imageMetaInformation.setName("NR1ZYO.jpg");
        imageMetaInformation.setSize(123123);
        imageMetaInformation.setType(IMAGE_JPEG_VALUE);
        imageMetaInformation.setUrl("/uploads/images/2016/08/01/NR1ZYO.jpg");
        imageMetaInformation.setThumbnailUrl("/uploads/images/2016/08/01/NR1ZYO.jpg");

        List<ImageMetaInformation> imageMetaInformations = new ArrayList<>();
        imageMetaInformations.add(imageMetaInformation);
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setFiles(imageMetaInformations);
        return ResponseEntity.ok(imageResponse);
    }

    @RequestMapping(value = "/videos", method = RequestMethod.POST)
    public String handleVideoUpload() {

        return null;
    }
}
