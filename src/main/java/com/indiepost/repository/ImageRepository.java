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

    ImageSet findByPrefix(String prefix);

    List<ImageSet> findByIds(List<Long> ids);

    List<ImageSet> findAll(Pageable pageable);

    List<ImageSet> findByPrefixes(List<String> prefixes);

    void update(ImageSet imageSet);

    void delete(ImageSet imageSet);

    void deleteById(Long id);

    ImageSet getReference(Long id);
}
