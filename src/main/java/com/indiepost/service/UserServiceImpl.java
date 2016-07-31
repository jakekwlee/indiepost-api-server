package com.indiepost.service;

import com.indiepost.model.User;
import com.indiepost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void add(User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(rawPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        User user = getUserByUsername(username, oldPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    @Override
    public User getUserById(int id) {
        return userRepository.findOne(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUserByUsername(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.findByUsernameAndPassword(username, encodedPassword);
    }

    @Override
    public boolean isUsernameExist(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean isEmailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<User> getUsersByState(User.State state) {
        return userRepository.findByState(state);
    }

    @Override
    public List<User> getUsersByGender(User.Gender gender) {
        return userRepository.findByGender(gender);
    }
}
