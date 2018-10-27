package com.indiepost.service

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.*
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion
import com.indiepost.config.AppConfig
import com.indiepost.config.AwsConfig
import com.indiepost.model.Image
import com.indiepost.model.ImageSet
import com.indiepost.repository.ImageRepository
import com.indiepost.repository.PostRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.inject.Inject

/**
 * Created by jake on 7/20/17.
 */
@Service
@Transactional
class ImageServiceAws @Inject constructor(
        imageRepository: ImageRepository,
        postRepository: PostRepository,
        appConfig: AppConfig,
        private val awsConfig: AwsConfig) : AbstractImageService(imageRepository, postRepository, appConfig), ImageService {

    private val s3Client: AmazonS3
        get() = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.AP_NORTHEAST_2)
                .build()

    @Throws(IOException::class)
    override fun saveUploadedImage(bufferedImage: BufferedImage, image: Image, contentType: String) {
        val fileExtension = contentType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(bufferedImage, fileExtension, outputStream)
        val buffer = outputStream.toByteArray()
        val inputStream = ByteArrayInputStream(buffer)

        val metadata = ObjectMetadata()
        metadata.contentType = contentType
        metadata.contentLength = buffer.size.toLong()

        s3Client.putObject(
                PutObjectRequest(
                        awsConfig.s3BucketName,
                        image.filePath!!.substring(1),
                        inputStream,
                        metadata
                ).withCannedAcl(CannedAccessControlList.PublicRead)
        )
    }

    @Throws(IOException::class)
    override fun delete(imageSet: ImageSet) {
        if (imageSet.images.isEmpty()) {
            return
        }
        val keys = imageSet.images.stream()
                .map { (_, filePath) -> KeyVersion(filePath!!.substring(1)) }
                .collect(Collectors.toList())

        val request = DeleteObjectsRequest(awsConfig.s3BucketName).withKeys(keys)
        val result: DeleteObjectsResult
        try {
            result = s3Client.deleteObjects(request)
            log.info("Successfully deleted all the {} items.", result.deletedObjects.size)
            imageRepository.delete(imageSet)
        } catch (mode: MultiObjectDeleteException) {
            printDeleteResults(mode)
        }

    }

    private fun printDeleteResults(deleteException: MultiObjectDeleteException) {
        log.error(deleteException.message)
        log.error("No. of objects successfully deleted = {}", deleteException.deletedObjects.size)
        log.error("No. of objects failed to delete = {}", deleteException.errors.size)
        log.error("Printing error data...{}")
        for (deleteError in deleteException.errors) {
            log.error("Object Key: {}\t{}\t{}",
                    deleteError.key, deleteError.code, deleteError.message)
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(ImageServiceAws::class.java)
    }
}
