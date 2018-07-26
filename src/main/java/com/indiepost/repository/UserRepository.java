package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface UserRepository {

    void save(User user);

    void delete(User user);

    User findById(Long id);

    User findByUsername(String username);

    List<User> search(String text, Types.UserRole role, Pageable pageable);

    User findCurrentUser();

    User findByEmail(String email);

    List<User> findAll(Pageable pageable);

    List<User> findByState(UserState state, Pageable pageable);

    List<User> findByGender(UserGender gender, Pageable pageable);

    List<User> findByUserRole(Types.UserRole role, Pageable pageable);

    Long getTotalUsers();

    Long getTotalUsers(Types.UserRole role);

    Long getTotalUsers(LocalDateTime from, LocalDateTime to);

    Long searchTotal(String text, Types.UserRole role);
}