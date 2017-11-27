package com.indiepost.repository;

import com.indiepost.model.Contributor;
import com.indiepost.model.QContributor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ContributorRepositoryHibernate implements ContributorRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Contributor findOne(Long id) {
        return entityManager.find(Contributor.class, id);
    }

    @Override
    public Long save(Contributor contributor) {
        entityManager.persist(contributor);
        entityManager.flush();
        return contributor.getId();
    }

    @Override
    public void delete(Contributor contributor) {
        entityManager.remove(contributor);
    }

    @Override
    public void deleteById(Long id) {
        Contributor contributor = entityManager.find(Contributor.class, id);
        entityManager.remove(contributor);
    }

    @Override
    public List<Contributor> findByIdIn(List<Long> ids) {
        QContributor contributor = QContributor.contributor;
        return getQueryFactory()
                .selectFrom(contributor)
                .where(contributor.id.in(ids))
                .fetch();
    }

    @Override
    public List<Contributor> findAllByOrderByCreatedAtAsc() {
        QContributor contributor = QContributor.contributor;
        return getQueryFactory()
                .selectFrom(contributor)
                .orderBy(contributor.createdAt.asc())
                .fetch();
    }

    @Override
    public List<Contributor> findAllByOrderByCreatedAtDesc() {
        QContributor contributor = QContributor.contributor;
        return getQueryFactory()
                .selectFrom(contributor)
                .orderBy(contributor.createdAt.desc())
                .fetch();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
