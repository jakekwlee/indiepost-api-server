package com.indiepost.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator of content type. This is simple and not complete implementation
 * of content type validating. It's based just on <code>String</code> equalsIgnoreCase
 * method.
 *
 * @author Michal Kreuzman
 */
public class ContentTypeMultipartFileValidator implements ConstraintValidator<ContentType, MultipartFile> {

    private String[] acceptedContentTypes;

    private static boolean acceptContentType(String contentType, String[] acceptedContentTypes) {
        for (String accept : acceptedContentTypes) {
            if (contentType.equalsIgnoreCase(accept)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void initialize(ContentType constraintAnnotation) {
        this.acceptedContentTypes = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty())
            return true;

        return ContentTypeMultipartFileValidator.acceptContentType(value.getContentType(), acceptedContentTypes);
    }
}
