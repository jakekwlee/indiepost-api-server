package com.indiepost.service;

import com.indiepost.dto.UserDto;
import com.indiepost.dto.UserUpdateDto;
import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import com.indiepost.repository.RoleRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.mapper.UserMapper.userDtoToUser;
import static com.indiepost.mapper.UserMapper.userToUserDto;

/**
 * Created by jake on 7/27/16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Inject
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(User user) {
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
    public User findCurrentUser() {
        return userRepository.findCurrentUser();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
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
        User user = findCurrentUser();
        return getUserDto(user);
    }

    @Override
    public UserDto getUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        userDto.setRoles(roles);
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
    public UserUpdateDto createOrUpdate(UserDto dto) {
        User user = findCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        // when user is new user
        if (user == null) {
            user = userDtoToUser(dto);
            user.setJoinedAt(now);
            user.setLastLogin(now);
            addRolesToUser(user, dto.getRoles());
            userRepository.save(user);
            return new UserUpdateDto(true, userToUserDto(user));
        }

        // TODO error 401
        if (!user.getUsername().equals(dto.getUsername())) {
            return null;
        }

        // user exists and not changed from last update
        if (dto.getUpdatedAt().isEqual(user.getUpdatedAt())) {
            user.setLastLogin(now);
            userRepository.update(user);
            return new UserUpdateDto(false, userToUserDto(user));
        }

        // copy to user entity
        user.setLastLogin(now);
        userDtoToUser(dto, user);
        addRolesToUser(user, dto.getRoles());
        userRepository.save(user);
        return new UserUpdateDto(false, userToUserDto(user));
    }

    private void addRolesToUser(User user, List<String> roles) {
        if (roles == null || roles.size() == 0) {
            return;
        }
        List<String> originalRoles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());
        if (!equalLists(originalRoles, roles)) {
            user.getRoles().clear();
            for (String r : roles) {
                Role role = roleRepository.findByUserRoleString(r);
                if (role == null) {
                    continue;
                }
                user.getRoles().add(role);
            }
        }

    }

    private boolean equalLists(List<String> one, List<String> two) {
        if (one == null && two == null) {
            return true;
        }

        if ((one == null && two != null)
                || one != null && two == null
                || one.size() != two.size()) {
            return false;
        }

        one = new ArrayList<>(one);
        two = new ArrayList<>(two);

        Collections.sort(one);
        Collections.sort(two);
        return one.equals(two);
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        Sort.Direction direction = isDesc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return new PageRequest(page, maxResults, direction, "joinedAt");
    }
}
