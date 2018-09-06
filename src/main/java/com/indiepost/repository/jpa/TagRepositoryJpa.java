package com.indiepost.repository.jpa;

import com.indiepost.model.QTag;
import com.indiepost.model.Tag;
import com.indiepost.repository.TagRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class TagRepositoryJpa implements TagRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QTag t = QTag.tag;

    @Override
    public void save(Tag tag) {
        entityManager.persist(tag);
    }

    @Override
    public Tag findByTagName(String name) {
        return getJPAQueryFactory()
                .selectFrom(t)
                .where(t.name.eq(name))
                .fetchOne();
    }

    @Override
    public Tag findById(Long id) {
        return getJPAQueryFactory()
                .selectFrom(t)
                .where(t.id.eq(id))
                .fetchOne();
    }

    @Override
    public List<Tag> findAll() {
        return getJPAQueryFactory()
                .selectFrom(t)
                .orderBy(t.name.asc())
                .fetch();
    }

    @Override
    public List<Tag> findAll(Pageable pageable) {
        return getJPAQueryFactory()
                .selectFrom(t)
                .orderBy(t.name.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Tag> findByNameIn(List<String> tagNames) {
        List<Tag> tags = getJPAQueryFactory()
                .selectFrom(t)
                .where(t.name.in(tagNames))
                .fetch();
        List<Tag> result = new ArrayList<>();
        for (String name : tagNames) {
            for (Tag tag : tags) {
                if (name.toLowerCase().equals(tag.getName().toLowerCase())) {
                    result.add(tag);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void delete(Tag tag) {
        entityManager.remove(tag);
    }

    private JPAQueryFactory getJPAQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
