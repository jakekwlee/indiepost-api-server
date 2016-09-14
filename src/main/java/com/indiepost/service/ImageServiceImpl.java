package com.indiepost.service;

import com.indiepost.enums.ImageEnum;
import com.indiepost.exception.FileSaveException;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.ImageRepository;
import com.indiepost.viewModel.ImageMeta;
import com.indiepost.viewModel.ImageResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
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

    private static final String API_URI = "/api/v1/images/";
    private static final String ROOT_DIRECTORY = "/data";
    private static final String BASE_URL = "/uploads/images";
    private static final int FILENAME_LENGTH = 6;
    private static final String FILENAME_FORMAT = "%s-%dx%d.%s";
    private static final String[] ACCEPTED_TYPES = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE};

    @Autowired
    private ImageRepository imageRepository;

    @Override
    public void save(ImageSet imageSet) {
        imageRepository.save(imageSet);
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

        String alphanumeric = RandomStringUtils.randomAlphanumeric(FILENAME_LENGTH);
        String fileExtension = contentType.split("/")[1];

        String newFilename;
        SimpleDateFormat dateFormat = new SimpleDateFormat("/yyyy/MM");
        String baseUrl = BASE_URL + dateFormat.format(new Date());
        String physicalBaseUrl = ROOT_DIRECTORY + baseUrl;

        ImageSet imageSet = new ImageSet();
        imageSet.setContentType(contentType);
        imageSet.setFeatured(false);
        imageSet.setUploadedAt(new Date());

        List<Image> images = new ArrayList<>();
        Image image;
        int width;
        int height;
        Dimension dimension;

        for (int i = 0; i < filesLength; ) {
            image = new Image();
            dimension = getDimension(multipartFiles[i]);
            width = (int) dimension.getWidth();
            height = (int) dimension.getHeight();
            image.setWidth(width);
            image.setHeight(height);
            newFilename = String.format(FILENAME_FORMAT, alphanumeric, (int) dimension.getWidth(), (int) dimension.getHeight(), fileExtension);
            long size = saveToFileSystem(multipartFiles[i], physicalBaseUrl + '/' + newFilename).length();

            image.setFileName(newFilename);
            image.setFileUrl(baseUrl + '/' + newFilename);
            image.setFileSize(size);

            if (i == 0) {
                image.setSizeType(ImageEnum.SizeType.Original);
                ++i;
            } else if (1200 <= width) {
                image.setSizeType(ImageEnum.SizeType.Large);
                width = 1000;
                ++i;
            } else if (700 <= width && width < 1200) {
                image.setSizeType(ImageEnum.SizeType.Medium);
                width = 500;
                ++i;
            } else if (400 <= width && width < 700) {
                image.setSizeType(ImageEnum.SizeType.Small);
                width = 300;
                ++i;
            } else if (width < 400) {
                image.setSizeType(ImageEnum.SizeType.Thumbnail);
                ++i;
            }
            image.setImageSet(imageSet);
            images.add(image);
        }
        imageSet.setImages(images);
        imageRepository.save(imageSet);
        return generateImageResponse(imageSet);
    }

    @Override
    public ImageSet findById(int id) {
        return imageRepository.findById(id);
    }

    @Override
    public ImageSet findByFileName(String fileName) {
        return imageRepository.findByFileName(fileName);
    }

    @Override
    public List<ImageSet> findAll(int page, int maxResults) {
        page = normalizePage(page);
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
    public JSONObject deleteById(int id) throws IOException {
        ImageSet imageSet = findById(id);
        JSONObject deletedFile = new JSONObject();
        deletedFile.put(imageSet.getOriginal().getFileName(), true);
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("files", deletedFile);

        delete(imageSet);
        return jsonResponse;
    }

    private int normalizePage(int page) {
        page = page < 1 ? 0 : page - 1;
        return page;
    }

    private boolean validateContentType(String contentType) {
        return Arrays.asList(ACCEPTED_TYPES).contains(contentType);
    }

    private ImageResponse generateImageResponse(ImageSet imageSet) {
        ImageMeta imageMeta = new ImageMeta();
        Image original = imageSet.getOriginal();
        Image thumbnail = imageSet.getThumbnail();
        imageMeta.setId(imageSet.getId());
        imageMeta.setName(original.getFileName());
        imageMeta.setSize(original.getFileSize());
        imageMeta.setType(imageSet.getContentType());
        imageMeta.setDeleteUrl(API_URI + imageSet.getId());
        imageMeta.setUrl(original.getFileUrl());

        if (thumbnail == null) {
            thumbnail = original;
        }
        imageMeta.setThumbnailUrl(thumbnail.getFileUrl());

        List<ImageMeta> imageMetaList = new ArrayList<>();
        imageMetaList.add(imageMeta);
        ImageResponse imageResponse = new ImageResponse();
        imageResponse.setFiles(imageMetaList);
        return imageResponse;
    }

    private Dimension getDimension(MultipartFile multipartFile) throws FileSaveException {
        try {
            ImageInputStream in = ImageIO.createImageInputStream(multipartFile.getInputStream());
            final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
            if (readers.hasNext()) {
                ImageReader reader = readers.next();
                try {
                    reader.setInput(in);
                    return new Dimension(reader.getWidth(0), reader.getHeight(0));
                } catch (IOException e) {

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

    private boolean deleteFileFromFileSystem(Image image) throws IOException {
        File fileToDelete = FileUtils.getFile(new File(ROOT_DIRECTORY + image.getFileUrl()));
        return FileUtils.deleteQuietly(fileToDelete);
    }
}
