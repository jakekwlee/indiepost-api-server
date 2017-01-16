package com.indiepost.repository;

import com.indiepost.model.ImageSet;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageRepository {

    void save(ImageSet imageSet);

    ImageSet findById(Long id);

    List<ImageSet> findByPostId(Long postId, Pageable pageable);

    List<ImageSet> findAll(Pageable pageable);

    void update(ImageSet imageSet);

    void delete(ImageSet imageSet);

    void deleteById(Long id);

    ImageSet getReference(Long id);
}
