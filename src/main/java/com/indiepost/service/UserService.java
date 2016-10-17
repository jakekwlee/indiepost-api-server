package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.User;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
public interface UserService {
    void save(User user);

    void update(User user);

    void delete(User user);

    void updatePassword(String username, String oldPassword, String newPassword);

    User getCurrentUser();

    String getCurrentUsername();

    User findById(Long id);

    User findByUsername(String username);

    User findByUsername(String username, String password);

    boolean isUsernameExist(String username);

    boolean isEmailExist(String email);

    List<User> findByState(UserEnum.State state);

    List<User> findByGender(UserEnum.Gender gender);

    List<User> findByRolesEnum(UserEnum.Roles role);
}
