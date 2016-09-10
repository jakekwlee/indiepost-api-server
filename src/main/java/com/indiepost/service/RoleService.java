package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.Role;

import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
public interface RoleService {
    void save(UserEnum.Roles role);

    void update(UserEnum.Roles role);

    void delete(UserEnum.Roles role);

    Role findById(int id);

    Role findByName(String name);

    List<Role> findAll();
}
