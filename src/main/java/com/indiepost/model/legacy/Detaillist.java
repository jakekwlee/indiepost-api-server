package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "detaillist")
public class Detaillist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private Long parent;

    private Long iorder;

    private Long type;

    private String data;

    private Long ispay;

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
}
