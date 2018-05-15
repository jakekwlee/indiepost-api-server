package com.indiepost.repository.jpa;

import com.indiepost.model.Post;
import com.indiepost.model.analytics.Pageview;
import com.indiepost.model.analytics.Visitor;
import com.indiepost.repository.PageviewRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PageviewRepositoryJpa implements PageviewRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Pageview pageview) {
        if (pageview.getPostId() != null) {
            Post postReference = entityManager.getReference(Post.class, pageview.getPostId());
            pageview.setPost(postReference);
        }
        if (pageview.getVisitorId() != null) {
            Visitor visitorReference = entityManager.getReference(Visitor.class, pageview.getVisitorId());
            pageview.setVisitor(visitorReference);
        }
        entityManager.persist(pageview);
    }
}
