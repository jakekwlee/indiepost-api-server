package com.indiepost.repository;

import com.indiepost.model.Role;
import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface UserRepository {

    void save(User user);

    void update(User user);

    void delete(User user);

    User findOne(int id);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);

    User findByEmail(String email);

    List<User> findByState(User.State state);

    List<User> findByGender(User.Gender gender);
}