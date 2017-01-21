package com.indiepost.repository;

import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.helper.CriteriaHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 8/17/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class ImageRepositoryHibernate implements ImageRepository {

    private final CriteriaHelper criteriaHelper;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ImageRepositoryHibernate(CriteriaHelper criteriaHelper) {
        this.criteriaHelper = criteriaHelper;
    }

    @Override
    public void save(ImageSet imageSet) {
        getSession().save(imageSet);

    }

    @Override
    public ImageSet findById(Long id) {
        return (ImageSet) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<ImageSet> findByIds(List<Long> ids) {
        return getCriteria()
                .createAlias("images", "images", JoinType.LEFT_OUTER_JOIN)
                .add(Restrictions.in("id", ids))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public List<ImageSet> findAll(Pageable pageable) {
        return getCriteria(pageable)
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
    }

    @Override
    public void update(ImageSet imageSet) {
        getSession().update(imageSet);
    }

    @Override
    public void delete(ImageSet imageSet) {
        getSession().delete(imageSet);
    }

    @Override
    public void deleteById(Long id) {
        delete(findById(id));
    }

    @Override
    public ImageSet getReference(Long id) {
        return entityManager.getReference(ImageSet.class, id);
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(ImageSet.class);
    }

    private Criteria getCriteriaForSingleImage() {
        return getSession().createCriteria(Image.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        return criteriaHelper.setPageToCriteria(getCriteria(), pageable);
    }
}
