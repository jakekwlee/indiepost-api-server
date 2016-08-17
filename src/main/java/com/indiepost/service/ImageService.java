package com.indiepost.service;

import com.indiepost.model.Image;
import com.indiepost.viewModel.ImageUploadForm;
import com.indiepost.viewModel.ImageResponse;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageService {

    void save(Image image);

    ImageResponse save(ImageUploadForm imageUploadForm);

    Image findById(int id);

    Image findByFilename(String filename);

    List<Image> findAll(int page, int maxResults);

    void update(Image image);

    void delete(Image image);

    void deleteById(int id);

}
