package com.indiepost.service;

import com.indiepost.config.ImageConfig;
import com.indiepost.enums.ImageEnum;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private ImageRepository imageRepository;

    private ImageConfig imageConfig;

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

            SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM");
            String baseUrl = imageConfig.getDbLocation() + dateFormat.format(new Date());

            ImageSet imageSet = new ImageSet();
            imageSet.setContentType(contentType);
            imageSet.setUploadedAt(new Date());
            List<Image> images = new ArrayList<>();

            BufferedImage originalBufferedImage = getBufferedImageFromMultipartFile(file);
            Image originalImage = saveUploadedImage(originalBufferedImage, ImageEnum.SizeType.ORIGINAL, alphanumeric, fileExtension, baseUrl);
            images.add(originalImage);

            BufferedImage largeBufferedImage = resizeImage(originalBufferedImage, 1200);
            Image largeImage = saveUploadedImage(largeBufferedImage, ImageEnum.SizeType.LARGE, alphanumeric, fileExtension, baseUrl);
            images.add(largeImage);

            BufferedImage optimizedBufferedImage = resizeImage(originalBufferedImage, 700);
            Image optimizedImage = saveUploadedImage(optimizedBufferedImage, ImageEnum.SizeType.OPTIMIZED, alphanumeric, fileExtension, baseUrl);
            images.add(optimizedImage);
            //TODO

            BufferedImage smallBufferedImage = resizeImage(optimizedBufferedImage, 400);
            Image smallImage = saveUploadedImage(smallBufferedImage, ImageEnum.SizeType.SMALL, alphanumeric, fileExtension, baseUrl);
            images.add(smallImage);

            BufferedImage thumbnailBufferedImage = generateThumbnail(smallBufferedImage, 120, 80);
            Image thumbnailImage = saveUploadedImage(thumbnailBufferedImage, ImageEnum.SizeType.THUMBNAIL, alphanumeric, fileExtension, baseUrl);
            images.add(thumbnailImage);

            imageSet.setImages(images);
            imageRepository.save(imageSet);
            imageSetList.add(imageSet);
        }
        return imageSetList;
    }

    private Image saveUploadedImage(BufferedImage bufferedImage, ImageEnum.SizeType sizeType,
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
    public List<ImageSet> findAll(int page, int maxResults) {
        return imageRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "uploadedAt"));
    }

    @Override
    public void update(ImageSet imageSet) {
        imageRepository.update(imageSet);
    }

    @Override
    public void delete(ImageSet imageSet) throws IOException {
        List<Image> images = imageSet.getImages();
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

    private boolean deleteFileFromFileSystem(Image image) throws IOException {
        File fileToDelete = FileUtils.getFile(new File(imageConfig.getFsRoot() + image.getFileUrl()));
        return FileUtils.deleteQuietly(fileToDelete);
    }
}
