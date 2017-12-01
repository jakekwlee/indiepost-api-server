package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.indiepost.enums.Types.ImageSize;
import com.indiepost.jackson.LocalDateTimeToUtcStringSerializer;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by jake on 9/7/16.
 */
@Entity
@Table(name = "ImageSets")
public class ImageSet implements Serializable {

    private static final long serialVersionUID = -6176638635434014551L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Fetch(FetchMode.JOIN)
    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL, CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "imageSetId")
    @BatchSize(size = 20)
    @JsonIgnore
    private Set<Image> images;

    @Column(nullable = false)
    @Size(min = 9, max = 10)
    private String contentType;

    @Size(max = 300)
    private String caption;

    @Size(max = 20)
    @Column(unique = true, nullable = false)
    private String prefix;

    @JsonSerialize(using = LocalDateTimeToUtcStringSerializer.class)
    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Image getOriginal() {
        return findByImageSize(ImageSize.ORIGINAL);
    }

    public Image getLarge() {
        return findByImageSize(ImageSize.LARGE);
    }

    public Image getOptimized() {
        return findByImageSize(ImageSize.OPTIMIZED);
    }

    public Image getSmall() {
        return findByImageSize(ImageSize.SMALL);
    }

    public Image getThumbnail() {
        return findByImageSize(ImageSize.THUMBNAIL);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private Image findByImageSize(ImageSize sizeType) {
        if (images == null) {
            return null;
        }
        for (Image image : images) {
            if (image.getSizeType() == sizeType) {
                return image;
            }
        }
        return null;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
