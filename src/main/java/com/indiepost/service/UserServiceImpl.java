package com.indiepost.service;

import com.indiepost.dto.user.SyncAuthorizationResponse;
import com.indiepost.dto.user.UserDto;
import com.indiepost.dto.user.UserProfileDto;
import com.indiepost.enums.Types.UserRole;
import com.indiepost.exceptions.UnauthorizedException;
import com.indiepost.model.ManagementToken;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import com.indiepost.model.UserKt;
import com.indiepost.repository.ManagementTokenRepository;
import com.indiepost.repository.RoleRepository;
import com.indiepost.repository.UserRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.indiepost.mapper.UserMapper.userDtoToUser;


/**
 * Created by jake on 7/27/16.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final ManagementTokenRepository tokenRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ManagementTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void update(UserProfileDto dto) {
        User user = findCurrentUser();
        if (user == null ||
                !user.getId().equals(dto.getId()) ||
                !user.getUsername().equals(dto.getUsername())) {
            throw new UnauthorizedException();
        }
        if (dto.getDisplayName() != null) {
            user.setDisplayName(dto.getDisplayName());
        }
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
            return new SyncAuthorizationResponse(true, UserKt.toDto(user));
        }

        if (!user.getUsername().equals(dto.getUsername())) {
            throw new UnauthorizedException();
        }

        user.setLastLogin(now);
        List<String> originalRoles = user.getRoles()
                .stream()
                .map(role -> role.getRoleType().toString())
                .collect(Collectors.toList());

        // if user roles have changed
        if (!equalLists(originalRoles, dto.getRoles())) {
            addRolesToUser(user, dto.getRoles());
            user.setUpdatedAt(now);
            userRepository.save(user);
        }
        return new SyncAuthorizationResponse(false, UserKt.toDto(user));
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

    private String getAuth0ManagementAPIAccessToken() {
        Optional<ManagementToken> token = tokenRepository.findById(1);
        if (token.isPresent() &&
                token.get().getExpireAt().isAfter(LocalDateTime.now())) {
            return token.get().getAccessToken();
        }

        try {
            // TODO
            HttpResponse<JsonNode> response = Unirest.post("https://indiepost.auth0.com/oauth/token")
                    .header("content-type", "application/json")
                    .body("{\"grant_type\":\"client_credentials\",\"client_id\": \"iJPAh5OMMR4jfFZRVYhX1TxZXFjGYDJK\",\"client_secret\": \"OpC17nCfe0LMGKG6m9deRNwK4FFflMJqX_Jrtcae2HxVwMBXMzTAfN2YDfdcTA5I\",\"audience\": \"https://indiepost.auth0.com/api/v2/\",\"scope\": \"update:users\"}")
                    .asJson();
            JSONObject jsonObject = response.getBody().getObject();
            String accessToken = (String) jsonObject.get("access_token");
            Integer expiresIn = (Integer) jsonObject.get("expires_in");
            LocalDateTime expireAt = LocalDateTime.now().plusSeconds(expiresIn);
            ManagementToken managementToken;

            if (token.isPresent()) {
                managementToken = token.get();
                managementToken.setAccessToken(accessToken);
                managementToken.setExpireAt(expireAt);
            } else {
                managementToken = new ManagementToken(accessToken, expireAt);
            }

            tokenRepository.save(managementToken);

            return accessToken;
        } catch (UnirestException e) {
            e.printStackTrace();
            throw new UnauthorizedException();
        }
    }

    private UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        List<String> roles = user.getRoles().stream()
                .map(role -> role.getRoleType().toString())
                .collect(Collectors.toList());
        userDto.setRoles(roles);
        userDto.setRoleType(user.getRoleType().toString());
        return userDto;
    }

    private void addRolesToUser(User user, List<String> roles) {
        if (roles == null || roles.size() == 0) {
            return;
        }
        user.getRoles().clear();
        int level = 0;
        UserRole roleType = null;
        for (String r : roles) {
            Role role = roleRepository.findByUserRoleString(r);
            if (role == null) {
                continue;
            }
            if (role.getLevel() > level) {
                roleType = role.getRoleType();
            }
            user.getRoles().add(role);
        }
        if (roleType != null) {
            user.setRoleType(roleType);
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
