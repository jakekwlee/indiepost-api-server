package com.indiepost.repository.jpa

import com.indiepost.model.Category
import com.indiepost.model.QCategory
import com.indiepost.repository.CategoryRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 8/4/16.
 */
@Repository
class CategoryRepositoryJpa : CategoryRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val c = QCategory.category

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun persist(category: Category) {
        entityManager.persist(category)
    }

    override fun delete(category: Category) {
        entityManager.remove(category)
    }


    override fun getReference(id: Long): Category {
        return entityManager.getReference(Category::class.java, id)
    }

    override fun findById(id: Long): Category? {
        return queryFactory
                .selectFrom(c)
                .where(c.id.eq(id))
                .fetchOne()
    }

    override fun findBySlug(slug: String): Category? {
        return queryFactory
                .selectFrom(c)
                .where(c.slug.eq(slug))
                .orderBy(c.displayOrder.asc())
                .fetchOne()
    }

    override fun findByParentId(parentId: Long): List<Category> {
        return queryFactory
                .selectFrom(c)
                .where(c.parentId.eq(parentId))
                .orderBy(c.displayOrder.asc())
                .fetch()
    }

    override fun findAll(): List<Category> {
        return queryFactory
                .selectFrom(c)
                .orderBy(c.displayOrder.asc())
                .fetch()
    }
}