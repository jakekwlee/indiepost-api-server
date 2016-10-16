package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "mycontentlist")
public class Mycontentlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private String userid;

    private String username;

    private Long type;

    private Long contentno;

    private String contenttitle;

    private String regdate;

    private String wuserid;

    private String wusername;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Long getContentno() {
        return contentno;
    }

    public void setContentno(Long contentno) {
        this.contentno = contentno;
    }

    public String getContenttitle() {
        return contenttitle;
    }

    public void setContenttitle(String contenttitle) {
        this.contenttitle = contenttitle;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getWuserid() {
        return wuserid;
    }

    public void setWuserid(String wuserid) {
        this.wuserid = wuserid;
    }

    public String getWusername() {
        return wusername;
    }

    public void setWusername(String wusername) {
        this.wusername = wusername;
    }
}
