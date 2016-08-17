package com.indiepost.repository;

import com.indiepost.model.Image;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
public interface ImageRepository {

    void save(Image image);

    Image findById(int id);

    Image findByFilename(String filename);

    List<Image> findByPostId(int id, Pageable pageable);

    List<Image> findAll(Pageable pageable);

    void update(Image image);

    void delete(Image image);

    void deleteById(int id);

}
