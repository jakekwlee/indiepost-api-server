package com.indiepost.service;

import com.indiepost.exception.FileSaveException;
import com.indiepost.model.Image;
import com.indiepost.repository.ImageRepository;
import com.indiepost.viewModel.ImageMetaInformation;
import com.indiepost.viewModel.ImageResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.*;

/**
 * Created by jake on 8/17/16.
 */
@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    public static final String ROOT_DIRECTORY = "/data";
    public static final String BASE_URL = "/uploads/images";
    public static final int FILENAME_LENGTH = 8;
    public static final String FILENAME_FORMAT = "%s-%dx%d.%s";
    public static final String[] ACCECTED_TYPES = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE};

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void save(Image image) {
        imageRepository.save(image);
    }

    @Override
    public ImageResponse saveUploadedImage(MultipartFile[] multipartFiles) throws FileSaveException {
        boolean isResizedOnClient = true;
        int filesLength = multipartFiles.length;

        if (filesLength == 0) {
            throw new FileSaveException("File does not uploaded.");
        }

        if (filesLength == 1) {
            isResizedOnClient = false;
        }

        String contentType = multipartFiles[0].getContentType();

        if (!validateContentType(contentType)) {
            throw new FileSaveException("File type is not accepted: " + contentType);
        }


        String alphanumeric = RandomStringUtils.randomAlphanumeric(FILENAME_LENGTH).toUpperCase();
        String fileExtension = FilenameUtils.getExtension(multipartFiles[0].getOriginalFilename());

        String newFilename;
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM");
        String baseUrl = BASE_URL + dateFormat.format(new Date());
        String physicalBaseUrl = ROOT_DIRECTORY + baseUrl;

        Image image = new Image();
        image.setDirectory(baseUrl);
        image.setUploadedAt(new Date());
        image.setFeatured(false);

        int width;
        int height;
        Dimension dimension;

        for (int i = 0; i < filesLength; ) {
            dimension = getDimension(multipartFiles[i]);
            width = (int) dimension.getWidth();
            height = (int) dimension.getHeight();
            image.setWidth(width);
            image.setHeight(height);
            newFilename = String.format(FILENAME_FORMAT, alphanumeric, (int) dimension.getWidth(), (int) dimension.getHeight(), fileExtension);
            long size = saveToFileSystem(multipartFiles[i], physicalBaseUrl + '/' + newFilename).length();

            if (i == 0) {
                image.setOriginal(newFilename);
                image.setFilesize(size);
                ++i;
            }
            else if (1200 < width) {
                image.setLarge(newFilename);
                width = 1000;
                ++i;
            }
            else if (700 < width && width < 1200) {
                image.setMedium(newFilename);
                width = 500;
                ++i;
            }
            else if (400 < width && width < 700) {
                image.setSmall(newFilename);
                width = 300;
                ++i;
            }
            else {
                image.setThumbnail(newFilename);
                ++i;
            }
        }
        imageRepository.save(image);
        return generateImageResponse(image);
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

    private boolean validateContentType(String contentType) {
        return Arrays.asList(ACCECTED_TYPES).contains(contentType);
    }

    private ImageResponse generateImageResponse(Image image) {
        ImageMetaInformation imageMetaInformation = new ImageMetaInformation();
        imageMetaInformation.setId(image.getId());
        imageMetaInformation.setName(image.getOriginal());
        imageMetaInformation.setType(image.getContentType());
        imageMetaInformation.setSize(image.getFilesize());
        imageMetaInformation.setDeleteUrl("/api/v1/images/" + image.getId());
        imageMetaInformation.setUrl(image.getLocation());

        if (image.getThumbnail() != null) {
            imageMetaInformation.setThumbnailUrl(image.getDirectory() + '/' + image.getThumbnail());
        } else {
            imageMetaInformation.setThumbnailUrl(image.getDirectory() + '/' + image.getOriginal());
        }

        List<ImageMetaInformation> imageMetaInformations = new ArrayList<>();
        imageMetaInformations.add(imageMetaInformation);
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setFiles(imageMetaInformations);
        return imageResponse;
    }

    private Dimension getDimension(MultipartFile multipartFile) throws FileSaveException {

        try (ImageInputStream in = ImageIO.createImageInputStream(multipartFile.getInputStream())) {
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } finally {
                    reader.dispose();
                }
            }
        } catch (IOException e) {
            throw new FileSaveException("Failed read image header: " + multipartFile.getOriginalFilename());
        }
        return null;
    }

    private File saveToFileSystem(MultipartFile multipartFile, String path) throws FileSaveException {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
            return file;
        } catch (IOException ioe) {
            throw new FileSaveException("Image Upload Failed: " + path, ioe);
        }
    }
}
