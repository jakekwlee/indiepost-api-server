package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.User;
import com.indiepost.repository.RoleRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
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
        User user = findByUsername(username, oldPassword);
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }


    @Override
    public User getCurrentUser() {
        String username = getCurrentUsername();
        return findByUsername(username);
    }

    @Override
    public String getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return securityContext.getAuthentication().getName();
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsername(String username, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.findByUsernameAndPassword(username, encodedPassword);
    }

    @Override
    public boolean isUsernameExist(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    @Override
    public boolean isEmailExist(String email) {
        User user = userRepository.findByEmail(email);
        return user != null;
    }

    @Override
    public List<User> findByState(UserEnum.State state) {
        return userRepository.findByState(state);
    }

    @Override
    public List<User> findByGender(UserEnum.Gender gender) {
        return userRepository.findByGender(gender);
    }

    @Override
    public List<User> findByRolesEnum(UserEnum.Roles role) {
        return new ArrayList<>(roleRepository.findByRolesEnum(role).getUsers());
    }
}
