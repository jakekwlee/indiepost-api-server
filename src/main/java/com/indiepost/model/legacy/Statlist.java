package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "statlist")
public class Statlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private String statdate;

    private Long contentno;

    private Long uv;

    private Long hit;

    private Long contentmenu;

    private Long buyprice;

    private Long device;

    private Long goods;

    private Long jjim;

    private Long subs;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getStatdate() {
        return statdate;
    }

    public void setStatdate(String statdate) {
        this.statdate = statdate;
    }

    public Long getContentno() {
        return contentno;
    }

    public void setContentno(Long contentno) {
        this.contentno = contentno;
    }

    public Long getUv() {
        return uv;
    }

    public void setUv(Long uv) {
        this.uv = uv;
    }

    public Long getHit() {
        return hit;
    }

    public void setHit(Long hit) {
        this.hit = hit;
    }

    public Long getContentmenu() {
        return contentmenu;
    }

    public void setContentmenu(Long contentmenu) {
        this.contentmenu = contentmenu;
    }

    public Long getBuyprice() {
        return buyprice;
    }

    public void setBuyprice(Long buyprice) {
        this.buyprice = buyprice;
    }

    public Long getDevice() {
        return device;
    }

    public void setDevice(Long device) {
        this.device = device;
    }

    public Long getGoods() {
        return goods;
    }

    public void setGoods(Long goods) {
        this.goods = goods;
    }

    public Long getJjim() {
        return jjim;
    }

    public void setJjim(Long jjim) {
        this.jjim = jjim;
    }

    public Long getSubs() {
        return subs;
    }

    public void setSubs(Long subs) {
        this.subs = subs;
    }
}
