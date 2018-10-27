package com.indiepost.repository.jpa

import com.indiepost.model.ImageSet
import com.indiepost.model.QImage
import com.indiepost.model.QImageSet
import com.indiepost.repository.ImageRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

/**
 * Created by jake on 8/17/16.
 */
@Repository
class ImageRepositoryJpa : ImageRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val i = QImageSet.imageSet

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(imageSet: ImageSet) {
        entityManager.persist(imageSet)
    }

    override fun findById(id: Long): ImageSet? {
        return queryFactory
                .selectFrom(i)
                .where(i.id.eq(id))
                .fetchOne()
    }

    override fun findByPrefix(prefix: String): ImageSet? {
        return queryFactory
                .selectFrom(i)
                .where(i.prefix.eq(prefix))
                .fetchOne()
    }

    override fun findByIdsIn(ids: List<Long>): List<ImageSet> {
        return queryFactory
                .selectFrom(i)
                .leftJoin(QImage.image)
                .where(i.id.`in`(ids))
                .fetch()
    }

    override fun findAll(pageable: Pageable): List<ImageSet> {
        return queryFactory
                .selectFrom(i)
                .orderBy(i.id.desc())
                .offset(pageable.offset)
                .limit(pageable.pageSize.toLong())
                .fetch()
    }

    override fun findByPrefixes(prefixes: Set<String>): List<ImageSet> {
        return queryFactory
                .selectFrom(i)
                .leftJoin(QImage.image).on(i.id.eq(QImage.image.imageSetId))
                .where(i.prefix.`in`(prefixes))
                .distinct()
                .fetch()
    }

    override fun delete(imageSet: ImageSet) {
        entityManager.remove(imageSet)
    }

    override fun deleteById(id: Long?) {
        val imageSetReference = entityManager.getReference(ImageSet::class.java, id)
        entityManager.remove(imageSetReference)
    }

    override fun count(): Long {
        return queryFactory.selectFrom(i).fetchCount()
    }
}
