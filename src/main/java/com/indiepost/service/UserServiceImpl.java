package com.indiepost.service;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.User;
import com.indiepost.repository.RoleRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
        // String username = getCurrentUsername();
        // For test
        String username = "indiepost";

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
    public List<User> findByState(UserEnum.State state, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return userRepository.findByState(state, pageable);
    }

    @Override
    public List<User> findByGender(UserEnum.Gender gender, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return userRepository.findByGender(gender, pageable);
    }

    @Override
    public List<User> findByRolesEnum(UserEnum.Roles role, int page, int maxResults, boolean isDesc) {
        return new ArrayList<>(roleRepository.findByRolesEnum(role).getUsers());
    }

    @Override
    public List<User> findAllUsers(int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return userRepository.findAll(pageable);
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new PageRequest(page, maxResults, direction, "joinedAt");
    }
}
