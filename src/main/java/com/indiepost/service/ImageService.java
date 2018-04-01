package com.indiepost.service;

import com.indiepost.dto.PostImageSetListDto;
import com.indiepost.model.ImageSet;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageService {

    void save(ImageSet imageSet);

    List<ImageSet> saveUploadedImages(MultipartFile[] multipartFiles) throws IOException, FileUploadException;

    ImageSet findById(Long id);

    ImageSet getReference(Long id);

    Page<ImageSet> findAll(Pageable pageable);

    PostImageSetListDto findImagesOnPost(Long postId);

    void update(ImageSet imageSet);

    void delete(ImageSet imageSet) throws IOException;

    Long deleteById(Long id) throws IOException;
}
