package com.indiepost.model.legacy;

import javax.persistence.*;

@Entity
@Table(name = "commentlist")
public class Commentlist {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long no;

    private Long parentno;

    private String userid;

    private String username;

    private String content;

    private String regdate;

    public Long getNo() {
        return no;
    }

    public void setNo(Long no) {
        this.no = no;
    }

    public Long getParentno() {
        return parentno;
    }

    public void setParentno(Long parentno) {
        this.parentno = parentno;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }
}
