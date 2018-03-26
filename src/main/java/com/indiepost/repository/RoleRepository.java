package com.indiepost.repository;

import com.indiepost.enums.Types.UserRole;
import com.indiepost.model.Role;

/**
 * Created by jake on 7/27/16.
 */
public interface RoleRepository {

    void save(UserRole role);

    void update(UserRole role);

    void delete(UserRole role);

    Role findById(Long id);

    Role findByUserRole(UserRole role);

    Role findByUserRoleString(String role);
}