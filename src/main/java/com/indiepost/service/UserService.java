package com.indiepost.service;

import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
public interface UserService {
    void add(User user);

    void update(User user);

    void delete(User user);

    void updatePassword(String username, String oldPassword, String newPassword);

    User getUserById(int id);

    User getUserByUsername(String username);

    User getUserByUsername(String username, String password);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);

    List<User> getUsers();

    List<User> getUsersByState(User.State state);

    List<User> getUsersByGender(User.Gender gender);
}
