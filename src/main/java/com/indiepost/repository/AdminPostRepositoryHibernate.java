package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import com.indiepost.repository.utils.CriteriaUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by jake on 17. 1. 11.
 */
@Repository
@SuppressWarnings("unchecked")
public class AdminPostRepositoryHibernate implements AdminPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Post post) {
        return (Long) getSession().save(post);
    }

    @Override
    public Post findById(Long id) {
        // TODO reduce query
        return entityManager.find(Post.class, id);
    }

    @Override
    public void update(Post post) {
        getSession().update(post);
    }

    @Override
    public void delete(Post post) {
        getSession().delete(post);
    }

    @Override
    public void deleteById(Long id) {
        Post post = findById(id);
        delete(post);
    }

    @Override
    public List<Post> find(User user, Pageable pageable) {
        return find(user, null, pageable);
    }

    @Override
    public List<Post> find(User user, PostQuery query, Pageable pageable) {
        Criteria criteria = getCriteria(pageable)
                .createAlias("creator", "creator")
                .createAlias("modifiedUser", "modifiedUser")
                .createAlias("category", "category");

        // TODO fetch n + 1 problem
        Projection projection = Projections.projectionList()
                .add(Property.forName("id"), "id")
                .add(Property.forName("title"), "title")
                .add(Property.forName("status"), "status")
                .add(Property.forName("displayName"), "displayName")
                .add(Property.forName("category.name"), "category.name")
                .add(Property.forName("creator.displayName"), "creator.displayName")
                .add(Property.forName("modifiedUser.displayName"), "modifiedUser.displayName")
                .add(Property.forName("createdAt"), "createdAt")
                .add(Property.forName("publishedAt"), "publishedAt")
                .add(Property.forName("modifiedAt"), "modifiedAt")
                .add(Property.forName("bookmarkCount"), "bookmarkCount");

        criteria.setProjection(projection);
        Conjunction conjunction = query == null ?
                Restrictions.conjunction() :
                CriteriaUtils.buildConjunction(query);

        switch (user.getHighestRole()) {
            case Administrator:
                break;
            case EditorInChief:
            case Editor:
                conjunction.add(getPrivacyCriterion(user.getId()));
                break;
            default:
                conjunction.add(getPrivacyCriterion(user.getId()));
                conjunction.add(Restrictions.eq("creatorId", user.getId()));
                break;
        }

        // if conjunction is not empty
        if (conjunction.conditions().iterator().hasNext()) {
            criteria.add(conjunction);
        }
        criteria.setResultTransformer(new FluentHibernateResultTransformer(Post.class));
        return criteria.list();
    }

    @Override
    public Long count() {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(PostQuery query) {
        Conjunction conjunction = CriteriaUtils.buildConjunction(query);
        return (Long) getCriteria()
                .add(conjunction)
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<Post> findScheduledPosts() {
        return getCriteria()
                .add(Restrictions.eq("status", PostStatus.FUTURE))
                .add(Restrictions.le("publishedAt", LocalDateTime.now()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public void disableSplashPosts() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Post> update = criteriaBuilder.createCriteriaUpdate(Post.class);
        Root e = update.from(Post.class);
        update.set("splash", false);
        update.where(criteriaBuilder.and(
                criteriaBuilder.equal(e.get("status"), PostStatus.PUBLISH)),
                criteriaBuilder.equal(e.get("splash"), true));
        entityManager.createQuery(update).executeUpdate();
    }

    @Override
    public void disableFeaturedPosts() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Post> update = criteriaBuilder.createCriteriaUpdate(Post.class);
        Root e = update.from(Post.class);
        update.set("featured", false);
        update.where(criteriaBuilder.and(
                criteriaBuilder.equal(e.get("status"), PostStatus.PUBLISH)),
                criteriaBuilder.equal(e.get("featured"), true));
        entityManager.createQuery(update).executeUpdate();
    }

    @Override
    public List<String> findAllDisplayNames() {
        return getCriteria()
                .add(Restrictions.ne("displayName", ""))
                .setProjection(
                        Projections.distinct(
                                Projections.projectionList()
                                        .add(Projections.property("displayName"))
                        )
                ).list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        return CriteriaUtils.setPageToCriteria(getCriteria(), pageable);
    }

    private Criterion getPrivacyCriterion(Long userId) {
        return Restrictions.not(
                Restrictions.and(
                        Restrictions.ne("modifiedUserId", userId),
                        Restrictions.or(
                                Restrictions.eq("status", PostStatus.TRASH),
                                Restrictions.eq("status", PostStatus.DRAFT),
                                Restrictions.eq("status", PostStatus.AUTOSAVE)
                        )
                )
        );
    }
}
