package com.indiepost.service;

import com.indiepost.dao.UserDAO;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDAO userDAO;

    // TODO: 7/27/16 make RoleDAO and wire
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void add(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userDAO.add(user);
    }

    @Override
    public void update(User user) {
        userDAO.update(user);
    }

    @Override
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username, oldPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userDAO.add(user);
    }


    @Override
    public User getUserById(int id) {
        return userDAO.getUserById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    @Override
    public User getUserByUsername(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = userDAO.getUserByUsername(username, encodedPassword);
        if (user == null) {
            // TODO: 7/27/16 throw user not found Exception
        }
        return user;
    }

    @Override
    public boolean isUsernameExist(String username) {
        return userDAO.isUsernameExist(username);
    }

    @Override
    public boolean isEmailExist(String email) {
        return userDAO.isEmailExist(email);
    }

    @Override
    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    @Override
    public List<User> getUsersByState(User.State state) {
        return null;
    }

    @Override
    public List<User> getUsersByGender(User.Gender gender) {
        return null;
    }

    @Override
    public List<User> getUsersByRole(Role role) {
        return null;
    }
}
