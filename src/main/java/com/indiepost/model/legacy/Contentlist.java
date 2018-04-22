package com.indiepost.model.legacy;

import org.springframework.web.util.HtmlUtils;

import javax.persistence.*;

@Entity
@Table(name = "contentlist")
public class Contentlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private String contentname;

    private String writername;

    private String writerid;

    private Integer menuno;

    private Integer type1no;

    private Integer type2no;

    private Integer os;

    private Integer platform;

    private String dataurl;

    private String regdate;

    private String modifydate;

    private String keyword;

    private Integer price;

    private Integer isarticleservice;

    private Integer isstreamingservice;

    private Integer isdownloadservice;

    private String imageurl;

    @Column(columnDefinition = "TEXT")
    private String contenttext;

    private Integer isdisplay;

    private Integer listseq;

    private Integer goods;

    private Integer listseqmain;

    private Integer ismain;

    private String imageurl2;

    private Integer uv;

    private Integer hit;

    private Integer jjim;

    private Integer subs;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public String getWritername() {
        return writername;
    }

    public void setWritername(String writername) {
        this.writername = writername;
    }

    public String getWriterid() {
        return writerid;
    }

    public void setWriterid(String writerid) {
        this.writerid = writerid;
    }

    public Integer getMenuno() {
        return menuno;
    }

    public void setMenuno(Integer menuno) {
        this.menuno = menuno;
    }

    public Integer getType1no() {
        return type1no;
    }

    public void setType1no(Integer type1no) {
        this.type1no = type1no;
    }

    public Integer getType2no() {
        return type2no;
    }

    public void setType2no(Integer type2no) {
        this.type2no = type2no;
    }

    public Integer getOs() {
        return os;
    }

    public void setOs(Integer os) {
        this.os = os;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public String getDataurl() {
        return dataurl;
    }

    public void setDataurl(String dataurl) {
        this.dataurl = dataurl;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getIsarticleservice() {
        return isarticleservice;
    }

    public void setIsarticleservice(Integer isarticleservice) {
        this.isarticleservice = isarticleservice;
    }

    public Integer getIsstreamingservice() {
        return isstreamingservice;
    }

    public void setIsstreamingservice(Integer isstreamingservice) {
        this.isstreamingservice = isstreamingservice;
    }

    public Integer getIsdownloadservice() {
        return isdownloadservice;
    }

    public void setIsdownloadservice(Integer isdownloadservice) {
        this.isdownloadservice = isdownloadservice;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getContenttext() {
        return contenttext;
    }

    public void setContenttext(String contenttext) {
        this.contenttext = HtmlUtils.htmlEscape(contenttext);
    }

    public Integer getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(Integer isdisplay) {
        this.isdisplay = isdisplay;
    }

    public Integer getListseq() {
        return listseq;
    }

    public void setListseq(Integer listseq) {
        this.listseq = listseq;
    }

    public Integer getGoods() {
        return goods;
    }

    public void setGoods(Integer goods) {
        this.goods = goods;
    }

    public Integer getListseqmain() {
        return listseqmain;
    }

    public void setListseqmain(Integer listseqmain) {
        this.listseqmain = listseqmain;
    }

    public Integer getIsmain() {
        return ismain;
    }

    public void setIsmain(Integer ismain) {
        this.ismain = ismain;
    }

    public String getImageurl2() {
        return imageurl2;
    }

    public void setImageurl2(String imageurl2) {
        this.imageurl2 = imageurl2;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Integer getHit() {
        return hit;
    }

    public void setHit(Integer hit) {
        this.hit = hit;
    }

    public Integer getJjim() {
        return jjim;
    }

    public void setJjim(Integer jjim) {
        this.jjim = jjim;
    }

    public Integer getSubs() {
        return subs;
    }

    public void setSubs(Integer subs) {
        this.subs = subs;
    }
}
