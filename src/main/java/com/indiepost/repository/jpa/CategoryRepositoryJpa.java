package com.indiepost.repository.jpa;

import com.indiepost.model.Category;
import com.indiepost.model.QCategory;
import com.indiepost.repository.CategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 8/4/16.
 */
@Repository
public class CategoryRepositoryJpa implements CategoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QCategory c = QCategory.category;

    @Override
    public void persist(Category category) {
        entityManager.persist(category);
    }

    @Override
    public void delete(Category category) {
        entityManager.remove(category);
    }


    @Override
    public Category getReference(Long id) {
        return entityManager.getReference(Category.class, id);
    }

    @Override
    public Category findById(Long id) {
        return getQueryFactory()
                .selectFrom(c)
                .where(c.id.eq(id))
                .fetchOne();
    }

    @Override
    public Category findBySlug(String slug) {
        return getQueryFactory()
                .selectFrom(c)
                .where(c.slug.eq(slug))
                .orderBy(c.displayOrder.asc())
                .fetchOne();
    }

    @Override
    public List<Category> findByParentId(Long parentId) {
        return getQueryFactory()
                .selectFrom(c)
                .where(c.parentId.eq(parentId))
                .orderBy(c.displayOrder.asc())
                .fetch();
    }

    @Override
    public List<Category> findAll() {
        return getQueryFactory()
                .selectFrom(c)
                .orderBy(c.displayOrder.asc())
                .fetch();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}