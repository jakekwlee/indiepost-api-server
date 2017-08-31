package com.indiepost.repository;

import com.indiepost.model.Setting;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by jake on 8/31/17.
 */
@Repository
public class SettingRepositoryHibernate implements SettingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Setting get() {
        return entityManager.find(Setting.class, 1L);
    }

    @Override
    public void update(Setting setting) {
        entityManager.merge(setting);
    }
}
