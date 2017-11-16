package com.indiepost.dto;

import com.indiepost.validation.ContentType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

import static org.springframework.util.MimeTypeUtils.*;


class MultipartImageUploadRequest {

    @NotNull
    @ContentType(value = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE}, message = "PNG나 JPG 포멧 이미지를 올려주세요!")
    private MultipartFile multipartFile;

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}