package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "bannerlist")
class Bannerlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private String url;

    private String image;

    private Long type;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }
}
