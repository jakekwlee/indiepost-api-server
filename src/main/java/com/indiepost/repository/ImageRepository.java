package com.indiepost.repository;

import com.indiepost.model.ImageSet;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageRepository {

    void save(ImageSet imageSet);

    ImageSet findById(int id);

    ImageSet findByFileName(String fileName);

    List<ImageSet> findByPostId(int id, Pageable pageable);

    List<ImageSet> findAll(Pageable pageable);

    void update(ImageSet imageSet);

    void delete(ImageSet imageSet);

    void deleteById(int id);

}
