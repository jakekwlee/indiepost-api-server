package com.indiepost.repository;

import com.github.fluent.hibernate.request.aliases.Aliases;
import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.repository.helper.CriteriaMaker;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Created by jake on 16. 10. 17.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostExcerptRepositoryHibernate implements PostExcerptRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaMaker criteriaMaker;

    @Override
    public Post findById(Long id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Override
    public List<Post> findByTitleLikes(String searchString, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByContentLikes(String searchString, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByTitleLikesOrContentLikes(String searchString, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByTagName(String tagName, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByTagIds(List<Long> tagIds, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByStatus(PostEnum.Status status, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByCategorySlug(String categorySlug, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByCategoryId(String categoryId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByAuthorName(String authorName, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByAuthorId(Long authorID, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByStatusAndAuthorId(PostEnum.Status status, Long authorId, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findByConditions(PostEnum.Status status, Long authorId, Long categoryId, List<Long> tagIds, String searchText, Pageable pageable) {
        return null;
    }

    @Override
    public List<Post> findAll(Long userId, Pageable pageable) {
        Criteria criteria = getCriteria(pageable);
        Aliases aliases = Aliases.create()
                .add("author", "a", JoinType.INNER)
                .add("editor", "e", JoinType.INNER)
                .add("category", "c", JoinType.INNER)
                .add("tags", "t", JoinType.INNER);
        aliases.addToCriteria(criteria);

        Criterion restrictions = Restrictions.not(
                Restrictions.and(
                        Restrictions.ne("author.id", userId),
                        Restrictions.or(
                                Restrictions.eq("status", PostEnum.Status.DELETED),
                                Restrictions.eq("status", PostEnum.Status.DRAFT)
                        )
                )
        );

        criteria.add(restrictions);
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        return criteria.list();
    }

    @Override
    public List<String> findAllAuthorNames() {
        Criteria criteria = getCriteria()
                .add(Restrictions.ne("displayName", ""))
                .setProjection(
                        Projections.distinct(
                                Projections.projectionList()
                                        .add(Projections.property("displayName"))

                        )
                );
        return criteria.list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        return criteriaMaker.getPagedCriteria(getCriteria(), pageable);
    }
}
