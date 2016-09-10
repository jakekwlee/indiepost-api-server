package com.indiepost.repository.hibernate;

import com.indiepost.model.Image;
import com.indiepost.model.ImageSet;
import com.indiepost.repository.CriteriaMaker;
import com.indiepost.repository.ImageRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
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
public class ImageRepositoryHibernate implements ImageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaMaker criteriaMaker;

    @Override
    public void save(ImageSet imageSet) {
        getSession().save(imageSet);

    }

    @Override
    public ImageSet findById(int id) {
        return (ImageSet) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public List<ImageSet> findByPostId(int id, Pageable pageable) {
        Criteria criteria = getCriteria(pageable)
                .add(Restrictions.eq("postId", id));

        return criteria.list();
    }

    @Override
    public List<ImageSet> findAll(Pageable pageable) {
        return getCriteria(pageable).list();
    }

    @Override
    public ImageSet findByFileName(String fileName) {
        Image image = (Image) getCriteriaForSingleImage().add(Restrictions.eq("fileName", fileName)).uniqueResult();
        if (image != null) {
            return image.getImageSet();
        }
        return null;
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
    public void deleteById(int id) {
        delete(findById(id));
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
        return criteriaMaker.getPagedCriteria(getCriteria(), pageable);
    }
}
