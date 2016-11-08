package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.indiepost.enums.ImageEnum.SizeType;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL, CascadeType.SAVE_UPDATE})
    @JoinColumn(name="imageSetId")
    private List<Image> images;

    @NotNull
    @Size(min = 9, max = 10)
    private String contentType;

    @NotNull
    private boolean isFeatured;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
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
        return findByImageSize(SizeType.Original);
    }

    public Image getLarge() {
        return findByImageSize(SizeType.Large);
    }

    public Image getMedium() {
        return findByImageSize(SizeType.Medium);
    }

    public Image getSmall() {
        return findByImageSize(SizeType.Small);
    }

    public Image getThumbnail() {
        return findByImageSize(SizeType.Thumbnail);
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

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
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
