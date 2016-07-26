package com.indiepost.dao.hibernate;

import com.indiepost.dao.UserDAO;
import com.indiepost.model.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jake on 7/27/16.
 */
@Repository
public class UserDAOHibernate implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public User getUserById(int id) {
        return (User) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public User getUserByUsername(String username) {
        return (User) getCriteria()
                .add(Restrictions.eq("username", username))
                .uniqueResult();
    }

    @Override
    public User getUserByUsername(String username, String password) {
        return (User) getCriteria()
                .add(Restrictions.eq("username", username))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public User getUserByEmail(String email) {
        return (User) getCriteria()
                .add(Restrictions.eq("email", email))
                .uniqueResult();
    }

    @Override
    public boolean isUsernameExist(String username) {
        int rowCount = ((Long) getCriteria().add(Restrictions.eq("username", username))
                .setProjection(Projections.rowCount()).uniqueResult()).intValue();
        if (rowCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmailExist(String email) {
        int rowCount = ((Long) getCriteria().add(Restrictions.eq("email", email))
                .setProjection(Projections.rowCount()).uniqueResult()).intValue();
        if (rowCount > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<User> getUsers() {
        return getCriteria().addOrder(Order.desc("id")).list();
    }

    @Override
    public List<User> getUsersByState(User.State state) {
        return getCriteria()
                .add(Restrictions.eq("state", state.toString()))
                .list();
    }

    @Override
    public List<User> getUsersByGender(User.Gender gender) {
        return getCriteria()
                .add(Restrictions.eq("gender", gender.toString()))
                .list();
    }

    @Override
    public void add(User user) {
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
    public int count() {
        return ((Long) getCriteria().setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    private Session getSession() {
        return this.sessionFactory.getCurrentSession();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(User.class);
    }
}
