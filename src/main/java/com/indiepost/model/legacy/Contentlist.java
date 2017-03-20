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

    private Long menuno;

    private Long type1no;

    private Long type2no;

    private Long os;

    private Long platform;

    private String dataurl;

    private String regdate;

    private String modifydate;

    private String keyword;

    private Long price;

    private Long isarticleservice;

    private Long isstreamingservice;

    private Long isdownloadservice;

    private String imageurl;

    private String contenttext;

    private Long isdisplay;

    private Long listseq;

    private Long goods;

    private Long listseqmain;

    private Long ismain;

    private String imageurl2;

    private Long uv;

    private Long hit;

    private Long jjim;

    private Long subs;

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

    public Long getMenuno() {
        return menuno;
    }

    public void setMenuno(Long menuno) {
        this.menuno = menuno;
    }

    public Long getType1no() {
        return type1no;
    }

    public void setType1no(Long type1no) {
        this.type1no = type1no;
    }

    public Long getType2no() {
        return type2no;
    }

    public void setType2no(Long type2no) {
        this.type2no = type2no;
    }

    public Long getOs() {
        return os;
    }

    public void setOs(Long os) {
        this.os = os;
    }

    public Long getPlatform() {
        return platform;
    }

    public void setPlatform(Long platform) {
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getIsarticleservice() {
        return isarticleservice;
    }

    public void setIsarticleservice(Long isarticleservice) {
        this.isarticleservice = isarticleservice;
    }

    public Long getIsstreamingservice() {
        return isstreamingservice;
    }

    public void setIsstreamingservice(Long isstreamingservice) {
        this.isstreamingservice = isstreamingservice;
    }

    public Long getIsdownloadservice() {
        return isdownloadservice;
    }

    public void setIsdownloadservice(Long isdownloadservice) {
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

    public Long getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(Long isdisplay) {
        this.isdisplay = isdisplay;
    }

    public Long getListseq() {
        return listseq;
    }

    public void setListseq(Long listseq) {
        this.listseq = listseq;
    }

    public Long getGoods() {
        return goods;
    }

    public void setGoods(Long goods) {
        this.goods = goods;
    }

    public Long getListseqmain() {
        return listseqmain;
    }

    public void setListseqmain(Long listseqmain) {
        this.listseqmain = listseqmain;
    }

    public Long getIsmain() {
        return ismain;
    }

    public void setIsmain(Long ismain) {
        this.ismain = ismain;
    }

    public String getImageurl2() {
        return imageurl2;
    }

    public void setImageurl2(String imageurl2) {
        this.imageurl2 = imageurl2;
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
