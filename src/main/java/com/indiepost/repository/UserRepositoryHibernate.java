package com.indiepost.repository;

import com.indiepost.enums.Types.UserGender;
import com.indiepost.enums.Types.UserState;
import com.indiepost.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.indiepost.repository.utils.CriteriaUtils.setPageToCriteria;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class UserRepositoryHibernate implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(User user) {
        getSession().save(user);
    }

    @Override
    public void update(User user) {
        getSession().update(user);
    }

    @Override
    public void delete(User user) {
        getSession().delete(user);
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return getCriteria(pageable).list();
    }

    @Override
    public User findById(Long id) {
        return (User) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public User findByUsername(String username) {
        return (User) getCriteria()
                .add(Restrictions.eq("username", username))
                .uniqueResult();
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        return (User) getCriteria()
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public User findByEmail(String email) {
        return (User) getCriteria()
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public List<User> findByState(UserState state, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("state", state))
                .list();
    }

    @Override
    public List<User> findByGender(UserGender gender, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("gender", gender))
                .list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(User.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        return setPageToCriteria(getCriteria(), pageable);
    }
}
