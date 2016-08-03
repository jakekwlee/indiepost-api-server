package com.indiepost.service;

import com.indiepost.model.Role;

import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
public interface RoleService {
    void save(Role role);

    void update(Role role);

    void delete(Role role);

    Role findById(int id);

    Role findByName(String name);

    List<Role> findAll();
}
