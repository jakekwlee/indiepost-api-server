package com.indiepost.repository;

import com.github.fluent.hibernate.request.aliases.Aliases;
import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.enums.PostEnum;
import com.indiepost.enums.UserEnum.Roles;
import com.indiepost.model.Post;
import com.indiepost.model.Role;
import com.indiepost.model.User;
import com.indiepost.repository.helper.CriteriaHelper;
import com.indiepost.dto.request.PostQuery;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.JoinType;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 1. 11.
 */
@Repository
@SuppressWarnings("unchecked")
public class AdminPostRepositoryHibernate implements AdminPostRepository {

    private final String[] ADMIN_PROJECTION = {
            "id",
            "title",
            "excerpt",
            "createdAt",
            "modifiedAt",
            "publishedAt",
            "displayName",
            "commentsCount",
            "likesCount",
            "postType",
            "status",
            "featuredImage",
            "author",
            "editor",
            "category",
            "tags"
    };

    private final CriteriaHelper criteriaHelper;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AdminPostRepositoryHibernate(CriteriaHelper criteriaHelper) {
        this.criteriaHelper = criteriaHelper;
    }

    @Override
    public Long save(Post post) {
        return (Long) getSession().save(post);
    }

    @Override
    public Post findById(Long id) {
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
    public List<Post> find(User user, Pageable pageable) {
        return find(user, null, pageable);
    }


    @Override
    public List<Post> find(User user, PostQuery query, Pageable pageable) {
        Criteria criteria = getPagedCriteria(pageable);
        getAliases().addToCriteria(criteria);
//        criteria.setProjection(criteriaHelper.buildProjectionList(ADMIN_PROJECTION));
        Roles role = getRole(user);
        Conjunction conjunction = Restrictions.conjunction();

        if (query != null) {
            criteriaHelper.buildConjunction(query, conjunction);
        }

        switch (role) {
            case Administrator:
                break;
            case EditorInChief:
            case Editor:
                conjunction.add(getPrivacyCriterion(user.getId()));
                break;
            default:
                conjunction.add(getPrivacyCriterion(user.getId()));
                conjunction.add(Restrictions.eq("author.id", user.getId()));
                break;
        }

        if (conjunction.conditions().iterator().hasNext()) {
            criteria.add(conjunction);
        }
        criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

        return criteria.list();
    }

    @Override
    public Long count() {
        return (Long) getCriteria().setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public Long count(PostQuery postQuery) {
        Conjunction conjunction = Restrictions.conjunction();
        criteriaHelper.buildConjunction(postQuery, conjunction);
        return (Long) getCriteria().add(conjunction).setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @Override
    public List<Post> findPostToPublish() {
        return getCriteria()
                .add(Restrictions.eq("status", PostEnum.Status.FUTURE))
                .add(Restrictions.le("publishedAt", new Date()))
                .setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public List<String> findAllDisplayNames() {
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

    private Criteria getPagedCriteria(Pageable pageable) {
        return criteriaHelper.setPageToCriteria(getCriteria(), pageable);
    }

    private Aliases getAliases() {
        return Aliases.create()
                .add("tags", "tags", JoinType.LEFT);
    }

    private Criterion getPrivacyCriterion(Long userId) {
        return Restrictions.not(
                Restrictions.and(
                        Restrictions.ne("editor.id", userId),
                        Restrictions.or(
                                Restrictions.eq("status", PostEnum.Status.TRASH),
                                Restrictions.eq("status", PostEnum.Status.DRAFT),
                                Restrictions.eq("status", PostEnum.Status.AUTOSAVE)
                        )
                )
        );
    }

    private Roles getRole(User user) {
        List<Role> roleList = user.getRoles();
        int userLevel = 1;
        for (Role role : roleList) {
            if (role.getLevel() > userLevel) {
                userLevel = role.getLevel();
            }
        }

        switch (userLevel) {
            case 9:
                return Roles.Administrator;
            case 7:
                return Roles.EditorInChief;
            case 5:
                return Roles.Editor;
            case 3:
                return Roles.Author;
            case 1:
                return Roles.User;
            default:
                return Roles.User;
        }
    }
}
