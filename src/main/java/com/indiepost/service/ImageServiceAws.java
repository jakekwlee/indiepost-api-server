package com.indiepost.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.indiepost.config.WebappConfig;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 7/20/17.
 */
@Service
@Transactional
public class ImageServiceAws extends ImageServiceAbstract implements ImageService {
    public ImageServiceAws(ImageRepository imageRepository, WebappConfig config) {
        super(imageRepository, config);
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
                        config.getAwsS3BucketName(),
                        image.getFilePath().substring(1),
                        inputStream,
                        metadata
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        );
    }

    @Override
    public void delete(ImageSet imageSet) throws IOException {
        DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(config.getAwsS3BucketName());

        List<KeyVersion> keys = imageSet.getImages().stream()
                .map(image -> new KeyVersion(image.getFilePath().substring(1)))
                .collect(Collectors.toCollection(ArrayList::new));

        multiObjectDeleteRequest.setKeys(keys);
        DeleteObjectsResult delObjRes = getS3Client().deleteObjects(multiObjectDeleteRequest);
        //TODO
        System.out.println(delObjRes.getDeletedObjects());
    }

    private AmazonS3 getS3Client() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(config.getAwsAccessKey(), config.getAwsSecretAccessKey());
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }
}
