package com.indiepost.repository.hibernate;

import com.indiepost.enums.PostEnum;
import com.indiepost.model.Post;
import com.indiepost.repository.PostExcerptRepository;
import com.indiepost.repository.helper.CriteriaMaker;
import com.indiepost.util.AliasToBeanNestedResultTransformer;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

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
        return null;
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
    public List<Post> findByTagIds(Set<Long> tagIds, Pageable pageable) {
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
    public List<Post> findByConditions(PostEnum.Status status, Long authorId, Long categoryId, Set<Long> tagIds, String searchText, Pageable pageable) {
        return null;
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        Criteria criteria = criteriaMaker.getPagedCriteria(getCriteria(), pageable);
        criteria.createAlias("author", "a")
                .createAlias("category", "c")
                .setProjection(Projections.projectionList()
                        .add(Property.forName("id"))
                        .add(Property.forName("title"))
                        .add(Property.forName("featuredImage"))
                        .add(Property.forName("excerpt"))
                        .add(Property.forName("authorName"))
                        .add(Property.forName("publishedAt"))
                        .add(Property.forName("likesCount"))
                        .add(Property.forName("commentsCount"))
                        .add(Property.forName("status"))
                        .add(Property.forName("a.id"), "author.id")
                        .add(Property.forName("a.username"), "author.username")
                        .add(Property.forName("a.email"), "author.email")
                        .add(Property.forName("a.displayName"), "author.displayName")
                        .add(Property.forName("c.id"), "category.id")
                        .add(Property.forName("c.name"), "category.name")
                        .add(Property.forName("c.slug"), "category.slug")
                );

        //// TODO: 16. 10. 17 impletement this!

        criteria.setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class));

        return criteria;
    }
}
