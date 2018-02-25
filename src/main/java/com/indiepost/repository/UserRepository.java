package com.indiepost.repository;

import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by jake on 7/26/16.
 */
public interface UserRepository {

    void save(User user);

    void update(User user);

    void delete(User user);

    User findById(Long id);

    User findByUsername(String username);

    User findCurrentUser();

    User findByEmail(String email);

    List<User> findAll(Pageable pageable);

    List<User> findByState(UserState state, Pageable pageable);

    List<User> findByGender(UserGender gender, Pageable pageable);
}