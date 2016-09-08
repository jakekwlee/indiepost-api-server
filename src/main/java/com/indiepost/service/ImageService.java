package com.indiepost.service;

import com.indiepost.exception.FileSaveException;
import com.indiepost.model.Image;
import com.indiepost.viewModel.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageService {

    void save(Image image);

    ImageResponse saveUploadedImage(MultipartFile[] multipartFiles) throws FileSaveException;

    Image findById(int id);

    Image findByFilename(String filename);

    List<Image> findAll(int page, int maxResults);

    void update(Image image);

    void delete(Image image);

    void deleteById(int id);

}
