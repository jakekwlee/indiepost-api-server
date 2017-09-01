package com.indiepost.model;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Created by jake on 8/31/17.
 */
@Entity
@Table(name = "Settings")
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Max(50)
    private String mailHost;

    @NotNull
    @Min(0)
    @Max(65535)
    private int mailPort;

    @NotNull
    @Email
    private String mailUsername;

    @NotNull
    @Max(50)
    private String mailPassword;

    @NotNull
    @Max(20)
    private String mailPersonalName;

    public String getMailPersonalName() {
        return mailPersonalName;
    }

    public void setMailPersonalName(String mailPersonalName) {
        this.mailPersonalName = mailPersonalName;
    }

    public int getMailPort() {
        return mailPort;
    }

    public void setMailPort(int mailPort) {
        this.mailPort = mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public void setMailUsername(String mailUsername) {
        this.mailUsername = mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public void setMailPassword(String mailPassword) {
        this.mailPassword = mailPassword;
    }

    public String getMailHost() {

        return mailHost;
    }

    public void setMailHost(String mailHost) {
        this.mailHost = mailHost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
