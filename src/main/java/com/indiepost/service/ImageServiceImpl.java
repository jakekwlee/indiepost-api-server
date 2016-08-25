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
        if (multipartFiles.length != 5) {
            throw new FileSaveException("File does not uploaded.");
        }

        MultipartFile originalImage = multipartFiles[0];
        String contentType = originalImage.getContentType();

        if (!validateContentType(contentType)) {
            throw new FileSaveException("File type is not accepted: " + originalImage.getContentType());
        }

        String alphanumeric = RandomStringUtils.randomAlphanumeric(FILENAME_LENGTH);
        String fileExtension = FilenameUtils.getExtension(originalImage.getOriginalFilename());

        Dimension dimension = getDimension(multipartFiles[0]);

        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();

        // ex) w9cS1WqP-1280x860.jpg
        String newFilename = String.format(FILENAME_FORMAT, alphanumeric, width, height, fileExtension);
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM");
        String baseUrl = BASE_URL + dateFormat.format(new Date());
        String physicalBaseUrl = ROOT_DIRECTORY + baseUrl;
        saveToFileSystem(multipartFiles[0], physicalBaseUrl + '/' + newFilename);

        Image image = new Image();
        image.setOriginal(newFilename);
        image.setDirectory(baseUrl);
        image.setFilesize(originalImage.getSize());
        image.setWidth(width);
        image.setHeight(height);
        image.setFeatured(false);
        image.setUploadedAt(new Date());

        if (width > 1200) {
            dimension = getDimension(multipartFiles[1]);
            newFilename = String.format(FILENAME_FORMAT, alphanumeric, (int) dimension.getWidth(), (int) dimension.getHeight(), fileExtension);
            saveToFileSystem(multipartFiles[1], physicalBaseUrl + '/' + newFilename);
            image.setLarge(newFilename);
        }
        if (width > 700) {
            dimension = getDimension(multipartFiles[2]);
            newFilename = String.format(FILENAME_FORMAT, alphanumeric, (int) dimension.getWidth(), (int) dimension.getHeight(), fileExtension);
            saveToFileSystem(multipartFiles[2], physicalBaseUrl + '/' + newFilename);
            image.setMedium(newFilename);
        }
        if (width > 400) {
            dimension = getDimension(multipartFiles[3]);
            newFilename = String.format(FILENAME_FORMAT, alphanumeric, (int) dimension.getWidth(), (int) dimension.getHeight(), fileExtension);
            saveToFileSystem(multipartFiles[3], physicalBaseUrl + '/' + newFilename);
            image.setSmall(newFilename);
        }

        dimension = getDimension(multipartFiles[4]);
        newFilename = String.format(FILENAME_FORMAT, alphanumeric, (int) dimension.getWidth(), (int) dimension.getHeight(), fileExtension);
        saveToFileSystem(multipartFiles[4], physicalBaseUrl + '/' + newFilename);
        image.setThumbnail(newFilename);

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
        imageMetaInformation.setThumbnailUrl(image.getDirectory() + '/' + image.getThumbnail());

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

    private void saveToFileSystem(MultipartFile multipartFile, String path) throws FileSaveException {
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            FileUtils.writeByteArrayToFile(file, multipartFile.getBytes());
        } catch (IOException ioe) {
            throw new FileSaveException("Image Upload Failed: " + path, ioe);
        }
    }
}
