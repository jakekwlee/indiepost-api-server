package com.indiepost.service

import com.indiepost.config.AppConfig
import com.indiepost.model.Image
import com.indiepost.model.ImageSet
import com.indiepost.repository.ImageRepository
import com.indiepost.repository.PostRepository
import org.apache.commons.io.FileUtils
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.attribute.PosixFilePermission
import java.util.*
import javax.imageio.ImageIO

/**
 * Created by jake on 7/20/17.
 */
class ImageServiceFileSystem(imageRepository: ImageRepository, postRepository: PostRepository, config: AppConfig) : AbstractImageService(imageRepository, postRepository, config) {

    @Throws(IOException::class)
    override fun saveUploadedImage(bufferedImage: BufferedImage, image: Image, contentType: String) {
        val path = config.staticRootPath + '/'.toString() + image.filePath
        val fileExtension = contentType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val file = File(path)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
            val directory = file.parentFile.absolutePath
            setFilePermission(directory)
        }
        ImageIO.write(bufferedImage, fileExtension, file)
        setFilePermission(path)
    }

    @Throws(IOException::class)
    override fun delete(imageSet: ImageSet) {
        val images = imageSet.images
        for ((_, filePath) in images) {
            val fileToDelete = FileUtils.getFile(File(config.staticRootPath + filePath!!))
            FileUtils.deleteQuietly(fileToDelete)
        }
        imageRepository.delete(imageSet)
    }

    @Throws(IOException::class)
    private fun setFilePermission(path: String) {
        val permissions = HashSet<PosixFilePermission>()
        //add owners permission
        permissions.add(PosixFilePermission.OWNER_READ)
        permissions.add(PosixFilePermission.OWNER_WRITE)
        permissions.add(PosixFilePermission.OWNER_EXECUTE)
        //add group permissions
        permissions.add(PosixFilePermission.GROUP_READ)
        permissions.add(PosixFilePermission.GROUP_EXECUTE)
        //add others permissions
        permissions.add(PosixFilePermission.OTHERS_READ)
        permissions.add(PosixFilePermission.OTHERS_EXECUTE)
        Files.setPosixFilePermissions(Paths.get(path), permissions)
    }
}
