package com.indiepost.service

import com.indiepost.config.AppConfig
import com.indiepost.dto.ImageSetDto
import com.indiepost.dto.PostImageSetDto
import com.indiepost.enums.Types.ImageSize
import com.indiepost.mapper.PostMapper.imageSetToDto
import com.indiepost.model.Image
import com.indiepost.model.ImageSet
import com.indiepost.repository.ImageRepository
import com.indiepost.repository.PostRepository
import com.indiepost.utils.DomUtil
import org.apache.commons.lang3.RandomStringUtils
import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.imgscalr.Scalr
import org.springframework.data.domain.*
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.stream.Collectors
import javax.imageio.ImageIO
import javax.inject.Inject

/**
 * Created by jake on 8/17/16.
 */
@Transactional
internal abstract class AbstractImageService @Inject constructor(
        val imageRepository: ImageRepository,
        private val postRepository: PostRepository,
        protected val config: AppConfig) : ImageService {

    @Throws(IOException::class)
    protected abstract fun saveUploadedImage(bufferedImage: BufferedImage, image: Image, contentType: String)

    @Throws(IOException::class)
    abstract override fun delete(imageSet: ImageSet?)

    override fun save(imageSet: ImageSet) {
        imageRepository.save(imageSet)
    }

    @Throws(IOException::class, FileUploadException::class)
    override fun saveUploadedImages(multipartFiles: Array<MultipartFile>): List<ImageSetDto> {
        if (multipartFiles.isEmpty()) {
            throw FileUploadException("File does not uploaded.")
        }
        val imageSetList = ArrayList<ImageSet>()

        for (file in multipartFiles) {
            val contentType = file.contentType
            validateContentType(contentType)

            val filenamePrefix = RandomStringUtils.randomAlphanumeric(config.imageFilenameLength)
            val dtFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd/")
            val imageSetPrefix = LocalDateTime.now().format(dtFormat) + filenamePrefix

            val imageSet = ImageSet()
            imageSet.prefix = imageSetPrefix
            imageSet.contentType = contentType
            imageSet.uploadedAt = LocalDateTime.now()
            val images = HashSet<Image>()

            var bufferedImage = ImageIO.read(file.inputStream)

            val originalImage = createImageObject(filenamePrefix, bufferedImage.width, bufferedImage.height, contentType!!, ImageSize.ORIGINAL)
            saveUploadedImage(bufferedImage, originalImage, contentType)
            images.add(originalImage)

            if (bufferedImage.width > 1440) {
                bufferedImage = resizeImage(bufferedImage, 1440)
                val largeImage = createImageObject(filenamePrefix, bufferedImage.width, bufferedImage.height, contentType, ImageSize.LARGE)
                saveUploadedImage(bufferedImage, largeImage, contentType)
                images.add(largeImage)
            }

            if (bufferedImage.width in 701..1440) {
                bufferedImage = resizeImage(bufferedImage, 700)
                val optimizedImage = createImageObject(filenamePrefix, bufferedImage.width, bufferedImage.height, contentType, ImageSize.OPTIMIZED)
                saveUploadedImage(bufferedImage, optimizedImage, contentType)
                images.add(optimizedImage)
            }

            if (bufferedImage.width in 401..700) {
                bufferedImage = resizeImage(bufferedImage, 400)
                val smallImage = createImageObject(filenamePrefix, bufferedImage.width, bufferedImage.height, contentType, ImageSize.SMALL)
                saveUploadedImage(bufferedImage, smallImage, contentType)
                images.add(smallImage)
            }


            bufferedImage = generateThumbnail(bufferedImage, 120, 80)
            val thumbnailImage = createImageObject(filenamePrefix, bufferedImage.width, bufferedImage.height, contentType, ImageSize.THUMBNAIL)
            saveUploadedImage(bufferedImage, thumbnailImage, contentType)
            images.add(thumbnailImage)

            imageSet.images = images
            imageRepository.save(imageSet)
            imageSetList.add(imageSet)
        }
        return imageSetList.stream()
                .map { imageSet -> imageSetToDto(imageSet) }
                .collect(Collectors.toList())
    }

    private fun createImageObject(filenamePrefix: String, width: Int, height: Int, contentType: String, sizeType: ImageSize): Image {
        val dtFormat = DateTimeFormatter.ofPattern("/yyyy/MM/dd/")
        val subPath = config.imageUploadPath + LocalDateTime.now().format(dtFormat)
        val fileExtension = contentType.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        val filename = String.format(config.imageFilenameFormat, filenamePrefix, width, height, fileExtension)

        val image = Image()
        image.fileName = filename
        image.filePath = subPath + filename
        image.width = width
        image.height = height
        image.sizeType = sizeType
        return image
    }

    override fun findById(id: Long?): ImageSet? {
        return imageRepository.findById(id!!)
    }

    override fun findAll(pageable: Pageable): Page<ImageSetDto> {
        val pageRequest = PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.Direction.DESC, "id")
        val imageSetList = imageRepository.findAll(pageRequest)
        val dtoList = imageSetList.stream()
                .map { imageSet -> imageSetToDto(imageSet) }
                .collect(Collectors.toList())
        val count = imageRepository.count()
        return PageImpl(dtoList, pageRequest, count!!)
    }

    override fun findImagesOnPost(postId: Long?): PostImageSetDto? {
        val post = postRepository.findById(postId!!)
        val dto = PostImageSetDto()
        if (post == null) {
            return null
        }
        val content = post.content

        val titleImage = post.titleImage
        val prefixList = DomUtil.getImagePrefixes(content)
        val imageSetList = imageRepository.findByPrefixes(prefixList)
        dto.titleImage = titleImage
        dto.images = imageSetList
        return dto
    }

    @Throws(IOException::class)
    override fun deleteById(id: Long?): Long? {
        val imageSet = findById(id)
        delete(imageSet)
        return id
    }

    private fun resizeImage(sourceImage: BufferedImage, definition: Int): BufferedImage {
        val width = sourceImage.width
        val resizedImage: BufferedImage
        resizedImage = if (width > definition) {
            Scalr.resize(sourceImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, definition)
        } else {
            sourceImage
        }
        return resizedImage
    }

    private fun generateThumbnail(originalImage: BufferedImage, width: Int, height: Int): BufferedImage {
        var x0 = 0
        var y0 = 0
        val resizedImage: BufferedImage

        val originalRatio = originalImage.width.toFloat() / originalImage.height
        val thumbnailRatio = width.toFloat() / height

        if (originalRatio >= thumbnailRatio) {
            resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_HEIGHT, height)
            x0 = Math.round((resizedImage.width - width) / 2.0).toInt()
        } else {
            resizedImage = Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_TO_WIDTH, width)
            y0 = Math.round((resizedImage.height - height) / 2.0).toInt()
        }

        return Scalr.crop(resizedImage, x0, y0, width, height)
    }

    @Throws(FileUploadException::class)
    private fun validateContentType(contentType: String?) {
        if (!config.acceptedImageTypes.contains(contentType)) {
            throw FileUploadException("File type is not accepted: " + contentType!!)
        }
    }
}
