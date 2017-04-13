package com.indiepost.service;

import com.indiepost.enums.Types.UserRole;
import com.indiepost.model.Role;

import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
public interface RoleService {
    void save(UserRole role);

    void update(UserRole role);

    void delete(UserRole role);

    Role findById(Long id);

    Role findByName(String name);

    List<Role> findAll();
}
