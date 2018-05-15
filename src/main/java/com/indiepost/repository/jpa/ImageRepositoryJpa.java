package com.indiepost.repository.jpa;

import com.indiepost.model.ImageSet;
import com.indiepost.model.QImage;
import com.indiepost.model.QImageSet;
import com.indiepost.repository.ImageRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 8/17/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class ImageRepositoryJpa implements ImageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QImageSet i = QImageSet.imageSet;

    @Override
    public void save(ImageSet imageSet) {
        entityManager.persist(imageSet);
    }

    @Override
    public ImageSet findById(Long id) {
        return getQueryFactory()
                .selectFrom(i)
                .where(i.id.eq(id))
                .fetchOne();
    }

    @Override
    public ImageSet findByPrefix(String prefix) {
        return getQueryFactory()
                .selectFrom(i)
                .where(i.prefix.eq(prefix))
                .fetchOne();
    }

    @Override
    public List<ImageSet> findByIdsIn(List<Long> ids) {
        return getQueryFactory()
                .selectFrom(i)
                .leftJoin(QImage.image)
                .where(i.id.in(ids))
                .fetch();
    }

    @Override
    public List<ImageSet> findAll(Pageable pageable) {
        return getQueryFactory()
                .selectFrom(i)
                .orderBy(i.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<ImageSet> findByPrefixes(Set<String> prefixes) {
        return getQueryFactory()
                .selectFrom(i)
                .leftJoin(QImage.image).on(i.id.eq(QImage.image.imageSetId))
                .where(i.prefix.in(prefixes))
                .distinct()
                .fetch();
    }

    @Override
    public void delete(ImageSet imageSet) {
        entityManager.remove(imageSet);
    }

    @Override
    public void deleteById(Long id) {
        ImageSet imageSetReference = entityManager.getReference(ImageSet.class, id);
        entityManager.remove(imageSetReference);
    }

    @Override
    public Long count() {
        return getQueryFactory().selectFrom(i).fetchCount();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
