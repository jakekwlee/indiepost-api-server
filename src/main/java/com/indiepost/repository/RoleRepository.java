package com.indiepost.repository;

import com.indiepost.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jake on 7/27/16.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRole(String role);
}
