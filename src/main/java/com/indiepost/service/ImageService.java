package com.indiepost.service;

import com.indiepost.exception.FileSaveException;
import com.indiepost.model.ImageSet;
import com.indiepost.viewModel.ImageResponse;
import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageService {

    void save(ImageSet imageSet);

    ImageResponse saveUploadedImage(MultipartFile[] multipartFiles) throws FileSaveException;

    ImageSet findById(Long id);

    ImageSet findByFileName(String fileName);

    List<ImageSet> findAll(int page, int maxResults);

    void update(ImageSet imageSet);

    void delete(ImageSet imageSet) throws IOException;

    JSONObject deleteById(Long id) throws IOException;
}
