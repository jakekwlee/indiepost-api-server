package com.indiepost.repository;

import com.indiepost.enums.Types;
import com.indiepost.model.Contributor;
import com.indiepost.model.QContributor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ContributorRepositoryHibernate implements ContributorRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private QContributor c = QContributor.contributor;

    @Override
    public Long save(Contributor contributor) {
        entityManager.persist(contributor);
        return contributor.getId();
    }

    @Override
    public void delete(Contributor contributor) {
        entityManager.remove(contributor);
    }

    @Override
    public void deleteById(Long id) {
        Optional contributor = findById(id);
        if (contributor.isPresent()) {
            entityManager.remove(contributor.get());
        }
    }

    @Override
    public Optional<Contributor> findById(Long id) {
        Contributor contributor = getQueryFactory()
                .selectFrom(c)
                .where(c.id.eq(id))
                .fetchOne();
        return contributor != null ? Optional.of(contributor) : Optional.empty();
    }

    @Override
    public Long count() {
        return getQueryFactory()
                .selectFrom(c)
                .fetchCount();
    }

    @Override
    public Page<Contributor> findAll(Pageable pageable) {
        List<Contributor> contributorList = getQueryFactory()
                .selectFrom(c)
                .orderBy(c.fullName.asc())
                .fetch();
        Long count = getQueryFactory()
                .selectFrom(c)
                .fetchCount();
        return new PageImpl<>(contributorList, pageable, count);
    }

    @Override
    public Page<Contributor> findAllByContributorType(Types.ContributorType contributorType, Pageable pageable) {
        List<Contributor> contributorList = getQueryFactory()
                .selectFrom(c)
                .where(c.contributorType.eq(contributorType))
                .orderBy(c.fullName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = countAllByContributorType(contributorType);
        return new PageImpl<>(contributorList, pageable, count);
    }

    @Override
    public Page<Contributor> findAllByFullName(String fullName, Pageable pageable) {
        List<Contributor> contributorList = getQueryFactory()
                .selectFrom(c)
                .where(c.fullName.like(fullName))
                .orderBy(c.fullName.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = getQueryFactory()
                .selectFrom(c)
                .where(c.fullName.like(fullName))
                .fetchCount();
        return new PageImpl<>(contributorList, pageable, count);
    }

    @Override
    public Page<Contributor> findByIdIn(List<Long> ids, Pageable pageable) {
        List<Contributor> contributorList = getQueryFactory()
                .selectFrom(c)
                .where(c.id.in(ids))
                .orderBy(c.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = getQueryFactory()
                .selectFrom(c)
                .where(c.id.in(ids))
                .fetchCount();
        return new PageImpl<>(contributorList, pageable, count);
    }

    @Override
    public Page<Contributor> findByFullNameIn(List<String> contributorNames, Pageable pageable) {
        List<Contributor> contributorList = getQueryFactory()
                .selectFrom(c)
                .where(c.fullName.in(contributorNames))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = getQueryFactory()
                .selectFrom(c)
                .where(c.fullName.in(contributorNames))
                .fetchCount();

        List<Contributor> result = new ArrayList<>();
        for (String name : contributorNames) {
            for (Contributor contributor : contributorList) {
                if (name.equals(contributor.getFullName())) {
                    result.add(contributor);
                }
            }
        }

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Long countAllByContributorType(Types.ContributorType contributorType) {
        return getQueryFactory()
                .selectFrom(c)
                .where(c.contributorType.eq(contributorType))
                .fetchCount();
    }


    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
