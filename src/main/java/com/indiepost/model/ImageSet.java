package com.indiepost.model;

import com.indiepost.enums.IndiepostEnum.ImageSizeType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by jake on 9/7/16.
 */
@Entity
@Table(name = "ImageSets")
public class ImageSet {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @OneToMany(mappedBy = "imageSet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images;

    @NotNull
    private String wildcardPrefix;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getWildcardPrefix() {
        return wildcardPrefix;
    }

    public void setWildcardPrefix(String wildcardPrefix) {
        this.wildcardPrefix = wildcardPrefix;
    }

    public Image getOriginal() {
        return findByImageSize(ImageSizeType.Original);
    }

    public Image getXLarge() {
        return findByImageSize(ImageSizeType.Xlarge);
    }

    public Image getLarge() {
        return findByImageSize(ImageSizeType.Large);
    }

    public Image getMedium() {
        return findByImageSize(ImageSizeType.Medium);
    }

    public Image getSmall() {
        return findByImageSize(ImageSizeType.Small);
    }

    public Image getThumbnail() {
        return findByImageSize(ImageSizeType.Thumbnail);
    }

    private Image findByImageSize(ImageSizeType sizeType) {
        for (Image image : images) {
            if (image.getSizeType() == sizeType) {
                return image;
            }
        }
        return null;
    }
}
