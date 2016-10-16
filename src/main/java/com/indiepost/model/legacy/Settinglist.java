package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "settinglist")
public class Settinglist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private Long type;

    private String title;

    private String content;

    private Long isdisplay;

    private Long isautopush;

    private String regdate;

    private String modifydate;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(Long isdisplay) {
        this.isdisplay = isdisplay;
    }

    public Long getIsautopush() {
        return isautopush;
    }

    public void setIsautopush(Long isautopush) {
        this.isautopush = isautopush;
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
}
