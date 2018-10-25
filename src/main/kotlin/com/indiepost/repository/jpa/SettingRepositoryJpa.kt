package com.indiepost.repository.jpa

import com.indiepost.model.Setting
import com.indiepost.repository.SettingRepository
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 8/31/17.
 */
@Repository
class SettingRepositoryJpa : SettingRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun get(): Setting {
        return entityManager.find(Setting::class.java, 1L)
    }

    override fun update(setting: Setting) {
        entityManager.merge(setting)
    }
}
