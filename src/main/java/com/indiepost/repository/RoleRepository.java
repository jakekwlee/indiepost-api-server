package com.indiepost.repository;

import com.indiepost.model.Role;
import com.indiepost.model.User;

/**
 * Created by jake on 7/27/16.
 */
public interface RoleRepository {

    void save(Role role);

    void update(Role role);

    void delete(Role role);

    Role findById(int id);

    Role findByRolesEnum(User.Roles role);
}