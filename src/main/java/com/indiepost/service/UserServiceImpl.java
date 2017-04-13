package com.indiepost.service;

import com.indiepost.dto.UserDto;
import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.User;
import com.indiepost.repository.RoleRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }
        return findByUsername(username);
    }

    @Override
    public String getCurrentUsername() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getName();
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
    public List<User> findByState(UserState state, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return userRepository.findByState(state, pageable);
    }

    @Override
    public List<User> findByGender(UserGender gender, int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return userRepository.findByGender(gender, pageable);
    }

    @Override
    public List<User> findByRolesEnum(UserRole role, int page, int maxResults, boolean isDesc) {
        return new ArrayList<>(roleRepository.findByUserRole(role).getUsers());
    }

    @Override
    public List<User> findAllUsers(int page, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(page, maxResults, isDesc);
        return userRepository.findAll(pageable);
    }

    @Override
    public UserDto getCurrentUserDto() {
        User user = getCurrentUser();
        return getUserDto(user);
    }

    @Override
    public List<UserDto> getDtoList(List<User> userList) {
        return userList
                .stream()
                .map(user -> {
                    UserDto userDto = new UserDto();
                    BeanUtils.copyProperties(user, userDto);
                    return userDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getDtoList(int page, int maxResults, boolean isDesc) {
        return getDtoList(this.findAllUsers(page, maxResults, isDesc));
    }

    @Override
    public List<UserDto> getDtoList(UserRole role, int page, int maxResults, boolean isDesc) {
        return getDtoList(this.findByRolesEnum(role, page, maxResults, isDesc));
    }

    @Override
    public UserDto getUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserDto(Long id) {
        User user = this.findById(id);
        return getUserDto(user);
    }

    @Override
    public UserDto getUserDto(String username) {
        User user = this.findByUsername(username);
        return getUserDto(user);
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new PageRequest(page, maxResults, direction, "joinedAt");
    }
}
