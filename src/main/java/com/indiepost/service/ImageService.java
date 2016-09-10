package com.indiepost.service;

import com.indiepost.exception.FileSaveException;
import com.indiepost.model.ImageSet;
import com.indiepost.viewModel.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageService {

    void save(ImageSet imageSet);

    ImageResponse saveUploadedImage(MultipartFile[] multipartFiles) throws FileSaveException;

    ImageSet findById(int id);

    ImageSet findByFileName(String fileName);

    List<ImageSet> findAll(int page, int maxResults);

    void update(ImageSet imageSet);

    void delete(ImageSet imageSet);

    void deleteById(int id);
}
