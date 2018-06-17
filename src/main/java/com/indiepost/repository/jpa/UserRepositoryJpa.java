package com.indiepost.repository.jpa;

import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.QUser;
import com.indiepost.model.User;
import com.indiepost.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class UserRepositoryJpa implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QUser u = QUser.user;

    @Override
    public void save(User user) {
        entityManager.persist(user);
        entityManager.flush();
    }

    @Override
    public void delete(User user) {
        entityManager.remove(user);
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return getQueryFactory()
                .selectFrom(u)
                .orderBy(u.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public User findById(Long id) {
        return getQueryFactory()
                .selectFrom(u)
                .where(u.id.eq(id))
                .fetchOne();
    }

    @Override
    public User findByUsername(String username) {
        return getQueryFactory()
                .selectFrom(u)
                .where(u.username.eq(username))
                .fetchOne();
    }

    @Override
    public User findByEmail(String email) {
        return getQueryFactory()
                .selectFrom(u)
                .where(u.email.eq(email))
                .fetchOne();
    }

    @Override
    public User findCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication == null) {
            return null;
        }
        String username = authentication.getName();
        return findByUsername(username);
    }

    @Override
    public List<User> findByState(UserState state, Pageable pageable) {
        return getQueryFactory()
                .selectFrom(u)
                .where(u.state.eq(state))
                .orderBy(u.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<User> findByGender(UserGender gender, Pageable pageable) {
        return getQueryFactory()
                .selectFrom(u)
                .where(u.gender.eq(gender))
                .orderBy(u.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
