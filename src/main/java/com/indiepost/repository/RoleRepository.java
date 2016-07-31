package com.indiepost.repository;

import com.indiepost.model.Role;

/**
 * Created by jake on 7/27/16.
 */
public interface RoleRepository {

    Role findByRole(String role);
}
