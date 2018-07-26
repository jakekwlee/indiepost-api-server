package com.indiepost.model;

import com.indiepost.enums.Types;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Entity
@Table(name = "Roles")
public class Role implements Serializable {

    private static final long serialVersionUID = 7196256370247012643L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Types.UserRole roleType;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

    private int level = 1;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Types.UserRole getRoleType() {
        return roleType;
    }

    public void setRoleType(Types.UserRole roleType) {
        this.roleType = roleType;
    }
}
