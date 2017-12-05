package com.indiepost.repository;

import com.indiepost.model.QTag;
import com.indiepost.model.Tag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TagRepositoryHibernate implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QTag qTag = QTag.tag;

    @Override
    public Tag findOne(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag findOneByName(String name) {
        return getJpaQuery()
                .selectFrom(qTag)
                .where(qTag.name.eq(name))
                .fetchOne();
    }

    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        entityManager.flush();
        return tag;
    }

    @Override
    public void deleteById(Long id) {
        Tag tag = findOne(id);
        entityManager.remove(tag);
    }

    @Override
    public List<Tag> findByIdIn(List<Long> ids) {
        List<Tag> tags = getJpaQuery()
                .selectFrom(qTag)
                .where(qTag.id.in(ids))
                .fetch();
        List<Tag> result = new ArrayList<>();
        for (Long id : ids) {
            for (Tag tag : tags) {
                if (id.equals(tag.getId())) {
                    result.add(tag);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<Tag> findAll(Pageable pageable) {
        return getJpaQuery()
                .selectFrom(qTag)
                .orderBy(qTag.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private JPAQueryFactory getJpaQuery() {
        return new JPAQueryFactory(entityManager);
    }
}
