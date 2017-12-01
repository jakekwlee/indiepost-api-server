package com.indiepost.model.legacy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "detaillist")
public class LegacyPostContent implements Serializable {

    private static final long serialVersionUID = 4590136339034273671L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    @ManyToOne
    @JoinColumn(name = "parent")
    private LegacyPost legacyPost;

    private Long iorder;

    private Long type;

    private String data;

    private Long ispay;

    public Long getNo() {
        return no;
    }

    public Long getIorder() {
        return iorder;
    }

    public void setIorder(Long iorder) {
        this.iorder = iorder;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getIspay() {
        return ispay;
    }

    public void setIspay(Long ispay) {
        this.ispay = ispay;
    }

    public LegacyPost getLegacyPost() {
        return legacyPost;
    }

    public void setLegacyPost(LegacyPost legacyPost) {
        this.legacyPost = legacyPost;
    }
}
