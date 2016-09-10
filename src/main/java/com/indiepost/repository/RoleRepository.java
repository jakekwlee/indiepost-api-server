package com.indiepost.repository;

import com.indiepost.enums.UserEnum.Roles;
import com.indiepost.model.Role;

/**
 * Created by jake on 7/27/16.
 */
public interface RoleRepository {

    void save(Roles role);

    void update(Roles role);

    void delete(Roles role);

    Role findById(int id);

    Role findByRolesEnum(Roles role);
}