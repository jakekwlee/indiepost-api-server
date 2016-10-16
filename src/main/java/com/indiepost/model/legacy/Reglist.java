package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "reglist")
public class Reglist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private String regdate;

    private String regid;

    private Long ispush;

    private Long onlywifi;

    private Long usewifi;

    private String appver;

    private Long isios;

    private String session_userid;

    private String session_username;

    private Long session_level;

    private String session_date;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public Long getIspush() {
        return ispush;
    }

    public void setIspush(Long ispush) {
        this.ispush = ispush;
    }

    public Long getOnlywifi() {
        return onlywifi;
    }

    public void setOnlywifi(Long onlywifi) {
        this.onlywifi = onlywifi;
    }

    public Long getUsewifi() {
        return usewifi;
    }

    public void setUsewifi(Long usewifi) {
        this.usewifi = usewifi;
    }

    public String getAppver() {
        return appver;
    }

    public void setAppver(String appver) {
        this.appver = appver;
    }

    public Long getIsios() {
        return isios;
    }

    public void setIsios(Long isios) {
        this.isios = isios;
    }

    public String getSession_userid() {
        return session_userid;
    }

    public void setSession_userid(String session_userid) {
        this.session_userid = session_userid;
    }

    public String getSession_username() {
        return session_username;
    }

    public void setSession_username(String session_username) {
        this.session_username = session_username;
    }

    public Long getSession_level() {
        return session_level;
    }

    public void setSession_level(Long session_level) {
        this.session_level = session_level;
    }

    public String getSession_date() {
        return session_date;
    }

    public void setSession_date(String session_date) {
        this.session_date = session_date;
    }
}
