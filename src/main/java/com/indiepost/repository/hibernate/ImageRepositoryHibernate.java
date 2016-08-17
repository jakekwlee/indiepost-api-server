package com.indiepost.repository.hibernate;

import com.indiepost.model.Image;
import com.indiepost.repository.CriteriaMaker;
import com.indiepost.repository.ImageRepository;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
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
    public void save(Image image) {
        getSession().save(image);

    }

    @Override
    public Image findById(int id) {
        return (Image) getCriteria()
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }

    @Override
    public Image findByFilename(String filename) {
        return (Image) getCriteria()
                .add(Restrictions.eq("filename", filename))
                .uniqueResult();
    }

    @Override
    public List<Image> findByPostId(int id, Pageable pageable) {
        Criteria criteria = getCriteria(pageable);
        criteria.setProjection(Projections.projectionList()
                .add(Property.forName("id"), "id")
                .add(Property.forName("directory"), "directory")
                .add(Property.forName("filename"), "filename")
                .add(Property.forName("width"), "width")
                .add(Property.forName("height"), "height")
                .add(Property.forName("isFeatured"), "isFeatured")
                .add(Property.forName("uploadedAt"), "uploadedAt")
        ).add(Restrictions.eq("postId", id));

        return criteria.list();
    }

    @Override
    public List<Image> findAll(Pageable pageable) {
        return getCriteria(pageable).list();
    }

    @Override
    public void update(Image image) {
        getSession().update(image);
    }

    @Override
    public void delete(Image image) {
        getSession().delete(image);
    }

    @Override
    public void deleteById(int id) {
        delete(findById(id));
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Image.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        return criteriaMaker.getPagedCriteria(getCriteria(), pageable);
    }
}
