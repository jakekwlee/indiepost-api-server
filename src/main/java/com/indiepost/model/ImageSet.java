package com.indiepost.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.indiepost.JsonView.Views;
import com.indiepost.enums.ImageEnum.SizeType;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.util.Date;
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
    @JsonView({Views.PublicList.class, Views.Admin.class})
    private Long id;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(orphanRemoval = true, fetch = FetchType.EAGER)
    @Cascade({CascadeType.ALL, CascadeType.SAVE_UPDATE})
    @JoinColumn(name = "imageSetId")
    @BatchSize(size = 50)
    private List<Image> images;

    @Column(nullable = false)
    @Size(min = 9, max = 10)
    @JsonView({Views.Admin.class})
    private String contentType;

    @Size(max = 300)
    private String caption;

    @Column(nullable = false)
    @JsonView({Views.Admin.class})
    private Date uploadedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @JsonView({Views.PublicList.class, Views.Admin.class})
    public Image getOriginal() {
        return findByImageSize(SizeType.ORIGINAL);
    }

    @JsonView({Views.PublicList.class, Views.Admin.class})
    public Image getLarge() {
        return findByImageSize(SizeType.LARGE);
    }

    @JsonView({Views.PublicList.class, Views.Admin.class})
    public Image getOptimized() {
        return findByImageSize(SizeType.OPTIMIZED);
    }

    @JsonView({Views.PublicList.class, Views.Admin.class})
    public Image getSmall() {
        return findByImageSize(SizeType.SMALL);
    }

    @JsonView({Views.PublicList.class, Views.Admin.class})
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
}
