package com.indiepost.service;

import com.indiepost.config.AppConfig;
import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.ImageRepository;
import com.indiepost.repository.PostRepository;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by jake on 7/20/17.
 */
public class ImageServiceFileSystem extends AbstractImageService {
    public ImageServiceFileSystem(ImageRepository imageRepository, PostRepository postRepository, AppConfig config) {
        super(imageRepository, postRepository, config);
    }

    @Override
    protected void saveUploadedImage(BufferedImage bufferedImage, Image image, String contentType) throws IOException {
        String path = config.getStaticRootPath() + '/' + image.getFilePath();
        String fileExtension = contentType.split("/")[1];
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            String directory = file.getParentFile().getAbsolutePath();
            setFilePermission(directory);
        }
        ImageIO.write(bufferedImage, fileExtension, file);
        setFilePermission(path);
    }

    @Override
    public void delete(ImageSet imageSet) throws IOException {
        Set<Image> images = imageSet.getImages();
        for (Image image : images) {
            File fileToDelete = FileUtils.getFile(new File(config.getStaticRootPath() + image.getFilePath()));
            FileUtils.deleteQuietly(fileToDelete);
        }
        imageRepository.delete(imageSet);
    }

    private void setFilePermission(String path) throws IOException {
        Set<PosixFilePermission> permissions = new HashSet<>();
        //add owners permission
        permissions.add(PosixFilePermission.OWNER_READ);
        permissions.add(PosixFilePermission.OWNER_WRITE);
        permissions.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        permissions.add(PosixFilePermission.GROUP_READ);
        permissions.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        permissions.add(PosixFilePermission.OTHERS_READ);
        permissions.add(PosixFilePermission.OTHERS_EXECUTE);
        Files.setPosixFilePermissions(Paths.get(path), permissions);
    }
}
