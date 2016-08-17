package com.indiepost.service;

import com.indiepost.model.Image;
import com.indiepost.repository.ImageRepository;
import com.indiepost.viewModel.ImageResponse;
import com.indiepost.viewModel.ImageUploadForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Override
    public ImageResponse save(ImageUploadForm imageUploadForm) {
        // TODO: 8/17/16 make image saving logic
        return null;
    }

    @Override
    public Image findById(int id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image findByFilename(String filename) {
        return imageRepository.findByFilename(filename);
    }

    @Override
    public List<Image> findAll(int page, int maxResults) {
        page = normalizePage(page);
        return imageRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "uploadedAt"));
    }

    @Override
    public void update(Image image) {
        imageRepository.update(image);
    }

    @Override
    public void delete(Image image) {
        imageRepository.delete(image);
    }

    @Override
    public void deleteById(int id) {
        imageRepository.deleteById(id);
    }

    private int normalizePage(int page) {
        page = page < 1 ? 0 : page - 1;
        return page;
    }
}
