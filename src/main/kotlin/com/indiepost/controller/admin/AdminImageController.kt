package com.indiepost.controller.admin

import com.indiepost.dto.DeleteResponse
import com.indiepost.dto.ImageSetDto
import com.indiepost.service.ImageService
import org.apache.tomcat.util.http.fileupload.FileUploadException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.inject.Inject

/**
 * Created by jake on 10/8/16.
 */
@RestController
@RequestMapping(value = ["/admin/images"], produces = arrayOf("application/json; charset=UTF-8"))
class AdminImageController @Inject
constructor(private val imageService: ImageService) {

    @RequestMapping(method = [RequestMethod.GET])
    fun getImages(pageable: Pageable): Page<ImageSetDto> {
        return imageService.findAll(pageable)
    }

    @RequestMapping(method = [RequestMethod.POST])
    @Throws(IOException::class, FileUploadException::class)
    fun handleImageUpload(@RequestParam("files") multipartFiles: Array<MultipartFile>): List<ImageSetDto> {
        return imageService.saveUploadedImages(multipartFiles)
    }

    @RequestMapping(value = ["/{id}"], method = [RequestMethod.DELETE])
    @Throws(IOException::class)
    fun handleImageDelete(@PathVariable id: Long): DeleteResponse {
        imageService.deleteById(id)
        return DeleteResponse(id)
    }
}
