package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "detaillist")
public class Detaillist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private Long parent;

    private Integer iorder;

    private Integer type;

    @Column(columnDefinition = "TEXT")
    private String data;

    private Integer ispay;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Integer getIorder() {
        return iorder;
    }

    public void setIorder(Integer iorder) {
        this.iorder = iorder;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getIspay() {
        return ispay;
    }

    public void setIspay(Integer ispay) {
        this.ispay = ispay;
    }
}
