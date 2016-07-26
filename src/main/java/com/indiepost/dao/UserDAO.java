package com.indiepost.dao;

import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface UserDAO {

    User getUserById(int id);

    User getUserByUsername(String username);

    User getUserByUsername(String username, String Password);

    User getUserByEmail(String email);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);

    List<User> getUsers();

    List<User> getUsersByState(User.State state);

    List<User> getUsersByGender(User.Gender gender);

    void add(User staff);

    void update(User staff);

    void delete(User staff);

    int count();
}
