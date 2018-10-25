package com.indiepost.repository.jpa

import com.indiepost.model.analytics.Action
import com.indiepost.model.analytics.Visitor
import com.indiepost.repository.ActionRepository
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ActionRepositoryJpa : ActionRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun save(action: Action) {
        if (action.visitorId != null) {
            val visitorReference = entityManager.getReference(Visitor::class.java, action.visitorId)
            action.visitor = visitorReference
        }
        entityManager.persist(action)
    }
}
