package com.indiepost.repository.jpa;

import com.indiepost.model.analytics.Action;
import com.indiepost.model.analytics.Visitor;
import com.indiepost.repository.ActionRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ActionRepositoryJpa implements ActionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Action action) {
        if (action.getVisitorId() != null) {
            Visitor visitorReference = entityManager.getReference(Visitor.class, action.getVisitorId());
            action.setVisitor(visitorReference);
        }
        entityManager.persist(action);
    }
}
