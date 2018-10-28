package com.indiepost.validation

import org.springframework.web.multipart.MultipartFile

import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

internal class ContentTypeMultipartFileValidator : ConstraintValidator<ContentType, MultipartFile> {

    private lateinit var acceptedContentTypes: Array<String>

    private fun isAcceptedContentType(contentType: String?): Boolean {
        for (accept in acceptedContentTypes) {
            if (contentType!!.equals(accept, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    override fun initialize(constraintAnnotation: ContentType?) {
        this.acceptedContentTypes = constraintAnnotation!!.value
    }

    override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
        return if (value == null || value.isEmpty) false else isAcceptedContentType(value.contentType)
    }
}
