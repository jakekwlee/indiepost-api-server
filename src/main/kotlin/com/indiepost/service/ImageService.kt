package com.indiepost.service

import com.indiepost.dto.ImageSetDto
import com.indiepost.dto.PostImageSetDto
import com.indiepost.model.ImageSet
import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile

import java.io.IOException

/**
 * Created by jake on 8/17/16.
 */
interface ImageService {

    fun save(imageSet: ImageSet)

    @Throws(IOException::class, FileUploadException::class)
    fun saveUploadedImages(multipartFiles: Array<MultipartFile>): List<ImageSetDto>

    fun findById(id: Long): ImageSet?

    fun findAll(pageable: Pageable): Page<ImageSetDto>

    fun findImagesOnPost(postId: Long): PostImageSetDto

    @Throws(IOException::class)
    fun delete(imageSet: ImageSet)

    @Throws(IOException::class)
    fun deleteById(id: Long): Long?
}
