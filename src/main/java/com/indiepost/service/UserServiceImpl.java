package com.indiepost.service;

import com.indiepost.dto.user.SyncAuthorizationResponse;
import com.indiepost.dto.user.UserDto;
import com.indiepost.dto.user.UserProfileDto;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.exceptions.UnauthorizedException;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import com.indiepost.repository.RoleRepository;
import com.indiepost.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    @Inject
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void update(UserProfileDto userProfileDto) {
        User user = findCurrentUser();
        if (user == null ||
                !user.getId().equals(userProfileDto.getId()) ||
                !user.getUsername().equals(userProfileDto.getUsername())) {
            throw new UnauthorizedException();
        }
        user.setDisplayName(userProfileDto.getDisplayName());
    }

    @Override
    public SyncAuthorizationResponse syncAuthorization(UserDto dto) {
        User user = findCurrentUser();
        LocalDateTime now = LocalDateTime.now();

        // if user is newly joined
        if (user == null) {
            user = userDtoToUser(dto);
            if (StringUtils.isEmpty(user.getDisplayName())) {
                user.setDisplayName("NO NAME");
            }
            user.setJoinedAt(now);
            user.setLastLogin(now);
            addRolesToUser(user, dto.getRoles());
            userRepository.save(user);
            return new SyncAuthorizationResponse(true, userToUserDto(user));
        }

        if (!user.getUsername().equals(dto.getUsername())) {
            throw new UnauthorizedException();
        }

        user.setLastLogin(now);
        List<String> originalRoles = user.getRoles()
                .stream()
                .map(role -> role.getName())
                .collect(Collectors.toList());

        // if user roles have changed
        if (!equalLists(originalRoles, dto.getRoles())) {
            addRolesToUser(user, dto.getRoles());
            user.setUpdatedAt(now);
            userRepository.save(user);
        }
        return new SyncAuthorizationResponse(false, userToUserDto(user));
    }


    @Override
    public User findCurrentUser() {
        return userRepository.findCurrentUser();
    }

    @Override
    public List<User> findByRole(UserRole role, int page, int maxResults, boolean isDesc) {
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
        return toUserDto(user);
    }

    private UserDto toUserDto(User user) {
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

    private void addRolesToUser(User user, List<String> roles) {
        if (roles == null || roles.size() == 0) {
            return;
        }
        user.getRoles().clear();
        for (String r : roles) {
            Role role = roleRepository.findByUserRoleString(r);
            if (role == null) {
                continue;
            }
            user.getRoles().add(role);
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
        return PageRequest.of(page, maxResults, direction, "joinedAt");
    }
}
