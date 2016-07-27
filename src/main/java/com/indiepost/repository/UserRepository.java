package com.indiepost.repository;

import com.indiepost.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User findByEmail(String email);

    List<User> findByState(User.State state);

    List<User> findByGender(User.Gender gender);
}