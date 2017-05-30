package com.indiepost.service;

import com.indiepost.config.ImageConfig;
import com.indiepost.enums.Types.ImageSize;
import com.indiepost.exception.FileSaveException;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.ImageRepository;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 8/17/16.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    private final ImageConfig imageConfig;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository, ImageConfig imageConfig) {
        this.imageRepository = imageRepository;
        this.imageConfig = imageConfig;
    }

    @Override
    public void save(ImageSet imageSet) {
        imageRepository.save(imageSet);
    }

    @Override
    public List<ImageSet> saveUploadedImages(MultipartFile[] multipartFiles) throws IOException {
        if (multipartFiles.length == 0) {
            throw new FileSaveException("File does not uploaded.");
        }
        List<ImageSet> imageSetList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String contentType = file.getContentType();
            if (!validateContentType(contentType)) {
                throw new FileSaveException("File type is not accepted: " + contentType);
            }
            String alphanumeric = RandomStringUtils.randomAlphanumeric(imageConfig.getFilenameLength());
            String fileExtension = contentType.split("/")[1];

            String baseUrl = imageConfig.getDbLocation() + LocalDateTime.now().format(DateTimeFormatter.ofPattern("/yyyy/MM"));

            ImageSet imageSet = new ImageSet();
            imageSet.setContentType(contentType);
            imageSet.setUploadedAt(LocalDateTime.now());
            Set<Image> images = new HashSet<>();

            BufferedImage bufferedImage = getBufferedImageFromMultipartFile(file);
            Image originalImage = saveUploadedImage(bufferedImage, ImageSize.ORIGINAL, alphanumeric, fileExtension, baseUrl);
            images.add(originalImage);

            if (bufferedImage.getWidth() > 1440) {
                bufferedImage = resizeImage(bufferedImage, 1440);
                Image largeImage = saveUploadedImage(bufferedImage, ImageSize.LARGE, alphanumeric, fileExtension, baseUrl);
                images.add(largeImage);
            }

            if (700 < bufferedImage.getWidth() && bufferedImage.getWidth() <= 1200) {
                bufferedImage = resizeImage(bufferedImage, 700);
                Image optimizedImage = saveUploadedImage(bufferedImage, ImageSize.OPTIMIZED, alphanumeric, fileExtension, baseUrl);
                images.add(optimizedImage);
            }

            if (400 < bufferedImage.getWidth() && bufferedImage.getWidth() <= 700) {
                bufferedImage = resizeImage(bufferedImage, 400);
                Image smallImage = saveUploadedImage(bufferedImage, ImageSize.SMALL, alphanumeric, fileExtension, baseUrl);
                images.add(smallImage);
            }


            bufferedImage = generateThumbnail(bufferedImage, 120, 80);
            Image thumbnailImage = saveUploadedImage(bufferedImage, ImageSize.THUMBNAIL, alphanumeric, fileExtension, baseUrl);
            images.add(thumbnailImage);

            imageSet.setImages(images);
            imageRepository.save(imageSet);
            imageSetList.add(imageSet);
        }
        return imageSetList;
    }

    private Image saveUploadedImage(BufferedImage bufferedImage, ImageSize sizeType,
                                    String alphanumeric, String fileExtension, String baseUrl) throws IOException {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        String physicalBaseUrl = imageConfig.getFsRoot() + baseUrl;
        String newFilename = String.format(imageConfig.getFilenameFormat(), alphanumeric, width, height, fileExtension);

        long size = saveToFileSystem(bufferedImage, fileExtension, physicalBaseUrl + '/' + newFilename).length();

        Image image = new Image();
        image.setFileName(newFilename);
        image.setFileUrl(baseUrl + '/' + newFilename);
        image.setFileSize(size);
        image.setWidth(width);
        image.setHeight(height);
        image.setSizeType(sizeType);
        return image;
    }

    @Override
    public ImageSet findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public ImageSet getReference(Long id) {
        return imageRepository.getReference(id);
    }

    @Override
    public List<ImageSet> findAll(int page, int maxResults) {
        return imageRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "uploadedAt"));
    }

    @Override
    public void update(ImageSet imageSet) {
        imageRepository.update(imageSet);
    }

    @Override
    public void delete(ImageSet imageSet) throws IOException {
        Set<Image> images = imageSet.getImages();
        for (Image image : images) {
            deleteFileFromFileSystem(image);
        }
        imageRepository.delete(imageSet);
    }

    @Override
    public Long deleteById(Long id) throws IOException {
        ImageSet imageSet = findById(id);
        delete(imageSet);
        return id;
    }

    private boolean validateContentType(String contentType) {
        return imageConfig.getAcceptedTypes().contains(contentType);
    }

    private BufferedImage getBufferedImageFromMultipartFile(MultipartFile file) throws IOException {
        return ImageIO.read(file.getInputStream());
    }

    private File saveToFileSystem(BufferedImage bufferedImage, String formatName, String path) throws IOException {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        ImageIO.write(bufferedImage, formatName, file);
        return file;
    }

    private BufferedImage resizeImage(BufferedImage sourceImage, int definition) {
        int width = sourceImage.getWidth();
        BufferedImage resizedImage;
        if (width > definition) {
            resizedImage = Scalr.resize(sourceImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, definition);
        } else {
            resizedImage = sourceImage;
        }
        return resizedImage;
    }

    private BufferedImage generateThumbnail(BufferedImage originalImage, int width, int height) {
        int x0 = 0;
        int y0 = 0;
        BufferedImage resizedImage;

        float originalRatio = (float) originalImage.getWidth() / originalImage.getHeight();
        float thumbnailRatio = (float) width / height;

        if (originalRatio >= thumbnailRatio) {
            resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height);
            x0 = (int) Math.round((resizedImage.getWidth() - width) / 2.0);
        } else {
            resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width);
            y0 = (int) Math.round((resizedImage.getHeight() - height) / 2.0);
        }

        return Scalr.crop(resizedImage, x0, y0, width, height);
    }

    private boolean deleteFileFromFileSystem(Image image) {
        File fileToDelete = FileUtils.getFile(new File(imageConfig.getFsRoot() + image.getFileUrl()));
        return FileUtils.deleteQuietly(fileToDelete);
    }
}
