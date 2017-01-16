package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.indiepost.enums.ImageEnum.SizeType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 9/7/16.
 */
@Entity
@Table(name = "ImageSets")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageSet {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL, CascadeType.SAVE_UPDATE})
    @JoinColumn(name="imageSetId")
    private List<Image> images;

    @NotNull
    @Size(min = 9, max = 10)
    private String contentType;

    @Size(max = 300)
    private String caption;

    @NotNull
    private Date uploadedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Image getOriginal() {
        return findByImageSize(SizeType.ORIGINAL);
    }

    public Image getLarge() {
        return findByImageSize(SizeType.LARGE);
    }

    public Image getOptimized() {
        return findByImageSize(SizeType.OPTIMIZED);
    }

    public Image getSmall() {
        return findByImageSize(SizeType.SMALL);
    }


    public Image getThumbnail() {
        return findByImageSize(SizeType.THUMBNAIL);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Date getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Date uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private Image findByImageSize(SizeType sizeType) {
        for (Image image : images) {
            if (image.getSizeType() == sizeType) {
                return image;
            }
        }
        return null;
    }
}
