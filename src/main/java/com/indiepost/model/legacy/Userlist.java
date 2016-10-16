package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "userlist")
public class Userlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private String userid;

    private String username;

    private String password;

    private Long level;

    private String regdate;

    private String email;

    private String hpp;

    private String uuid;

    private Long ny;

    private String birthday;

    private Long buyinfo1;

    private Long buyinfo2;

    private String accesslist;

    private Long point;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHpp() {
        return hpp;
    }

    public void setHpp(String hpp) {
        this.hpp = hpp;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getNy() {
        return ny;
    }

    public void setNy(Long ny) {
        this.ny = ny;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getBuyinfo1() {
        return buyinfo1;
    }

    public void setBuyinfo1(Long buyinfo1) {
        this.buyinfo1 = buyinfo1;
    }

    public Long getBuyinfo2() {
        return buyinfo2;
    }

    public void setBuyinfo2(Long buyinfo2) {
        this.buyinfo2 = buyinfo2;
    }

    public String getAccesslist() {
        return accesslist;
    }

    public void setAccesslist(String accesslist) {
        this.accesslist = accesslist;
    }

    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }
}
