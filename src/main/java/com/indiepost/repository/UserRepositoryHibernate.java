package com.indiepost.repository;

import com.indiepost.enums.UserEnum;
import com.indiepost.model.User;
import com.indiepost.repository.helper.CriteriaMaker;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class UserRepositoryHibernate implements UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaMaker criteriaMaker;

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
                .add(Restrictions.eq("pawword", password))
                .uniqueResult();
    }

    @Override
    public User findByEmail(String email) {
        return (User) getCriteria()
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public List<User> findByState(UserEnum.State state, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("state", state))
                .list();
    }

    @Override
    public List<User> findByGender(UserEnum.Gender gender, Pageable pageable) {
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
        return criteriaMaker.getPagedCriteria(getCriteria(), pageable);
    }
}
