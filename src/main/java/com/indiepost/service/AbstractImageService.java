package com.indiepost.service;

import com.indiepost.config.AppConfig;
import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.PostImageSetDto;
import com.indiepost.enums.Types.ImageSize;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.model.Post;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import com.indiepost.utils.DomUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.imgscalr.Scalr;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.indiepost.mapper.PostMapper.imageSetToDto;

/**
 * Created by jake on 8/17/16.
 */
@Transactional
abstract class AbstractImageService implements ImageService {

    protected final AppConfig config;

    final ImageRepository imageRepository;

    private final PostRepository postRepository;

    @Inject
    public AbstractImageService(ImageRepository imageRepository, PostRepository postRepository, AppConfig config) {
        this.imageRepository = imageRepository;
        this.config = config;
        this.postRepository = postRepository;
    }

    abstract protected void saveUploadedImage(BufferedImage bufferedImage, Image image, String contentType) throws IOException;

    @Override
    abstract public void delete(ImageSet imageSet) throws IOException;

    @Override
    public void save(ImageSet imageSet) {
        imageRepository.save(imageSet);
    }

    @Override
    public List<ImageSetDto> saveUploadedImages(MultipartFile[] multipartFiles) throws IOException, FileUploadException {
        if (multipartFiles.length == 0) {
            throw new FileUploadException("File does not uploaded.");
        }
        List<ImageSet> imageSetList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {
            String contentType = file.getContentType();
            validateContentType(contentType);

            String filenamePrefix = RandomStringUtils.randomAlphanumeric(config.getImageFilenameLength());
            DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd/");
            String imageSetPrefix = LocalDateTime.now().format(dtFormat) + filenamePrefix;

            ImageSet imageSet = new ImageSet();
            imageSet.setPrefix(imageSetPrefix);
            imageSet.setContentType(contentType);
            imageSet.setUploadedAt(LocalDateTime.now());
            Set<Image> images = new HashSet<>();

            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

            Image originalImage = createImageObject(filenamePrefix, bufferedImage.getWidth(), bufferedImage.getHeight(), contentType, ImageSize.ORIGINAL);
            saveUploadedImage(bufferedImage, originalImage, contentType);
            images.add(originalImage);

            if (bufferedImage.getWidth() > 1440) {
                bufferedImage = resizeImage(bufferedImage, 1440);
                Image largeImage = createImageObject(filenamePrefix, bufferedImage.getWidth(), bufferedImage.getHeight(), contentType, ImageSize.LARGE);
                saveUploadedImage(bufferedImage, largeImage, contentType);
                images.add(largeImage);
            }

            if (700 < bufferedImage.getWidth() && bufferedImage.getWidth() <= 1440) {
                bufferedImage = resizeImage(bufferedImage, 700);
                Image optimizedImage = createImageObject(filenamePrefix, bufferedImage.getWidth(), bufferedImage.getHeight(), contentType, ImageSize.OPTIMIZED);
                saveUploadedImage(bufferedImage, optimizedImage, contentType);
                images.add(optimizedImage);
            }

            if (400 < bufferedImage.getWidth() && bufferedImage.getWidth() <= 700) {
                bufferedImage = resizeImage(bufferedImage, 400);
                Image smallImage = createImageObject(filenamePrefix, bufferedImage.getWidth(), bufferedImage.getHeight(), contentType, ImageSize.SMALL);
                saveUploadedImage(bufferedImage, smallImage, contentType);
                images.add(smallImage);
            }


            bufferedImage = generateThumbnail(bufferedImage, 120, 80);
            Image thumbnailImage = createImageObject(filenamePrefix, bufferedImage.getWidth(), bufferedImage.getHeight(), contentType, ImageSize.THUMBNAIL);
            saveUploadedImage(bufferedImage, thumbnailImage, contentType);
            images.add(thumbnailImage);

            imageSet.setImages(images);
            imageRepository.save(imageSet);
            imageSetList.add(imageSet);
        }
        return imageSetList.stream()
                .map(imageSet -> imageSetToDto(imageSet))
                .collect(Collectors.toList());
    }

    private Image createImageObject(String filenamePrefix, int width, int height, String contentType, ImageSize sizeType) {
        DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("/yyyy/MM/dd/");
        String subPath = config.getImageUploadPath() + LocalDateTime.now().format(dtFormat);
        String fileExtension = contentType.split("/")[1];
        String filename = String.format(config.getImageFilenameFormat(), filenamePrefix, width, height, fileExtension);

        Image image = new Image();
        image.setFileName(filename);
        image.setFilePath(subPath + filename);
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
    public Page<ImageSetDto> findAll(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC, "id");
        List<ImageSet> imageSetList = imageRepository.findAll(pageRequest);
        List<ImageSetDto> dtoList = imageSetList.stream()
                .map(imageSet -> imageSetToDto(imageSet))
                .collect(Collectors.toList());
        Long count = imageRepository.count();
        return new PageImpl<>(dtoList, pageRequest, count);
    }

    @Override
    public PostImageSetDto findImagesOnPost(Long postId) {
        Post post = postRepository.findById(postId);
        PostImageSetDto PostImageSetDto = new PostImageSetDto();
        if (post == null) {
            return null;
        }
        String content = post.getContent();

        ImageSet titleImage = post.getTitleImage();
        int size = titleImage.getImages().size();
        Set<String> prefixList = DomUtil.getImagePrefixes(content);
        List<ImageSet> imageSetList = imageRepository.findByPrefixes(prefixList);
        PostImageSetDto.setTitleImage(titleImage);
        PostImageSetDto.setImages(imageSetList);
        return PostImageSetDto;
    }

    @Override
    public Long deleteById(Long id) throws IOException {
        ImageSet imageSet = findById(id);
        delete(imageSet);
        return id;
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

    private void validateContentType(String contentType) throws FileUploadException {
        if (!config.getAcceptedImageTypes().contains(contentType)) {
            throw new FileUploadException("File type is not accepted: " + contentType);
        }
    }
}
