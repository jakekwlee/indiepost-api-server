package com.indiepost.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.indiepost.config.AppConfig;
import com.indiepost.config.AwsConfig;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 7/20/17.
 */
@Service
@Transactional
public class ImageServiceAws extends AbstractImageService implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceAws.class);
    private final AwsConfig awsConfig;

    @Autowired
    public ImageServiceAws(ImageRepository imageRepository, PostRepository postRepository, AppConfig appConfig, AwsConfig awsConfig) {
        super(imageRepository, postRepository, appConfig);
        this.awsConfig = awsConfig;
    }

    @Override
    protected void saveUploadedImage(BufferedImage bufferedImage, Image image, String contentType) throws IOException {
        String fileExtension = contentType.split("/")[1];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, fileExtension, outputStream);
        byte[] buffer = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(buffer);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(buffer.length);

        getS3Client().putObject(
                new PutObjectRequest(
                        awsConfig.getS3BucketName(),
                        image.getFilePath().substring(1),
                        inputStream,
                        metadata
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    @Override
    public void delete(ImageSet imageSet) throws IOException {
        List<KeyVersion> keys = imageSet.getImages().stream()
                .map(image -> new KeyVersion(image.getFilePath().substring(1)))
                .collect(Collectors.toList());

        DeleteObjectsRequest request = new DeleteObjectsRequest(awsConfig.getS3BucketName()).withKeys(keys);
        DeleteObjectsResult result;
        try {
            result = getS3Client().deleteObjects(request);
            log.info("Successfully deleted all the {} items.", result.getDeletedObjects().size());
            imageRepository.delete(imageSet);
        } catch (MultiObjectDeleteException mode) {
            printDeleteResults(mode);
        }
    }

    private void printDeleteResults(MultiObjectDeleteException deleteException) {
        log.error(deleteException.getMessage());
        log.error("No. of objects successfully deleted = {}", deleteException.getDeletedObjects().size());
        log.error("No. of objects failed to delete = {}", deleteException.getErrors().size());
        log.error("Printing error data...{}");
        for (MultiObjectDeleteException.DeleteError deleteError : deleteException.getErrors()) {
            log.error("Object Key: {}\t{}\t{}",
                    deleteError.getKey(), deleteError.getCode(), deleteError.getMessage());
        }
    }

    private AmazonS3 getS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
