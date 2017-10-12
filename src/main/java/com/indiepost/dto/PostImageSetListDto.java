package com.indiepost.dto;

import com.indiepost.model.ImageSet;

import java.util.List;

/**
 * Created by jake on 10/12/17.
 */
public class PostImageSetListDto {
    private ImageSet titleImage;
    private List<ImageSet> images;

    public ImageSet getTitleImage() {
        return titleImage;
    }

    public void setTitleImage(ImageSet titleImage) {
        this.titleImage = titleImage;
    }

    public List<ImageSet> getImages() {
        return images;
    }

    public void setImages(List<ImageSet> images) {
        this.images = images;
    }
}
