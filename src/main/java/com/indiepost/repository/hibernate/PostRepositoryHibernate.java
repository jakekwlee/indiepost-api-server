package com.indiepost.repository.hibernate;

import com.indiepost.model.Category;
import com.indiepost.model.Post;
import com.indiepost.model.User;
import com.indiepost.repository.PostRepository;
import com.indiepost.util.AliasToBeanNestedResultTransformer;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by jake on 7/30/16.
 */
@Repository
public class PostRepositoryHibernate implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post findById(int id) {
        return (Post) getCriteria()
                .add(Restrictions.eq("id", id))
                //.setFetchMode("mediaContents", FetchMode.JOIN)
                .uniqueResult();
    }

    @Override
    public void save(Post post) {
        getSession().save(post);
    }


    @Override
    public void delete(Post post) {
        getSession().delete(post);
    }

    @Override
    public void update(Post post) {
        getSession().update(post);
    }

    @Override
    public List<Post> findByCategory(Category category, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("categoryId", category.getId()))
                .list();
    }

    @Override
    public List<Post> findByAuthor(User author, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("authorId", author.getId()))
                .list();
    }

    @Override
    public List<Post> findByEditor(User editor, Pageable pageable) {
        return getCriteria(pageable)
                .add(Restrictions.eq("editorId", editor.getId()))
                .list();
    }

    @Override
    public List<Post> findAll(Pageable pageable) {
        return getCriteria(pageable).list();
    }

    @Override
    public List<Post> findAllForUser(Pageable pageable) {
        return getCriteriaForHomeUser(pageable)
                .setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class))
                .list();
    }

    @Override
    public Post findByIdForUser(int id) {
        Post post = findById(id);
        Post postForUser = new Post();
        postForUser.setId(post.getId());
        postForUser.setTitle(post.getTitle());
        postForUser.setContent(post.getContent());
        postForUser.setPublishedAt(post.getPublishedAt());
        postForUser.setAuthor(post.getAuthor());
        postForUser.setCategory(post.getCategory());
        postForUser.setMediaContents(post.getMediaContents());
        return postForUser;
    }

    @Override
    public List<Post> findByCategoryForUser(Category category, Pageable pageable) {
        return getCriteriaForHomeUser(pageable)
                .add(Restrictions.eq("categoryId", category.getId()))
                .setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class))
                .list();
    }

    @Override
    public List<Post> findByCategorySlugForUser(String slug, Pageable pageable) {
        return getCriteriaForHomeUser(pageable)
                .add(Restrictions.eq("c.slug", slug))
                .setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class))
                .list();
    }

    @Override
    public List<Post> findByAuthorForUser(User author, Pageable pageable) {
        return getCriteriaForHomeUser(pageable)
                .add(Restrictions.eq("a.id.", author.getId()))
                .setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class))
                .list();
    }

    @Override
    public List<Post> findByAuthorUsernameForUser(String username, Pageable pageable) {
        return getCriteriaForHomeUser(pageable)
                .add(Restrictions.eq("a.username", username))
                .setResultTransformer(new AliasToBeanNestedResultTransformer(Post.class))
                .list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Post.class);
    }

    private Criteria getCriteria(Pageable pageable) {
        Criteria criteria = getCriteria();
        Sort sort = pageable.getSort();
        if (sort != null) {
            for (Sort.Order order : sort) {
                if (order.getDirection().equals(Sort.Direction.ASC)) {
                    criteria.addOrder(Order.asc(order.getProperty()));
                } else {
                    criteria.addOrder(Order.desc(order.getProperty()));
                }
            }
        }
        criteria.setFirstResult(pageable.getOffset());
        criteria.setMaxResults(pageable.getPageSize());
        return criteria;
    }

    private Criteria getCriteriaForHomeUser(Pageable pageable) {
        Criteria criteria = getCriteria(pageable);
        criteria.createAlias("author", "a")
                .createAlias("editor", "e")
                .createAlias("category", "c")
                .setProjection(Projections.projectionList()
                        .add(Property.forName("id"), "id")
                        .add(Property.forName("title"), "title")
                        .add(Property.forName("titleImage"), "titleImage")
                        .add(Property.forName("excerpt"), "excerpt")
                        .add(Property.forName("publishedAt"), "publishedAt")
                        .add(Property.forName("likesCount"), "likesCount")
                        .add(Property.forName("a.displayName"), "author.displayName")
                        .add(Property.forName("e.displayName"), "editor.displayName")
                        .add(Property.forName("c.name"), "category.name")
                        .add(Property.forName("c.slug"), "category.slug")
                )
                .add(Restrictions.eq("status", Post.Status.PUBLISHED));
        return criteria;
    }

}
