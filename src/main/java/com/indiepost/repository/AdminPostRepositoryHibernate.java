package com.indiepost.repository;

import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostSearch;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.repository.utils.CriteriaUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public List<AdminPostSummaryDto> find(User currentUser, Pageable pageable) {
        return this.find(currentUser, null, pageable);
    }

    @Override
    public List<AdminPostSummaryDto> find(User currentUser, PostSearch search, Pageable pageable) {
        QPost post = QPost.post;
        JPAQuery query = getQueryFactory().from(post);

        addProjections(query)
                .innerJoin(post.creator, QUser.user)
                .innerJoin(post.modifiedUser, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .orderBy(post.publishedAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .distinct();

        BooleanBuilder builder = new BooleanBuilder();
        Long userId = currentUser.getId();
        switch (currentUser.getHighestRole()) {
            case Administrator:
                break;
            case EditorInChief:
            case Editor:
                builder.or(post.creatorId.eq(userId))
                        .or(post.status.eq(PostStatus.PUBLISH))
                        .or(post.status.eq(PostStatus.FUTURE))
                        .or(post.status.eq(PostStatus.PENDING));
                break;
            default:
                builder.and(post.creatorId.eq(userId));
                break;
        }
        if (search != null) {
            CriteriaUtils.addSearchConjunction(search, builder);
        }
        query.where(builder);

        return toDtoList(query.fetch());
    }

    @Override
    public List<String> findAllDisplayNames() {
        QPost post = QPost.post;
        return getQueryFactory()
                .from(post)
                .select(post.bylineName)
                .where(post.bylineName.isNotEmpty())
                .distinct()
                .fetch();
    }

    @Override
    public Long count() {
        return getQueryFactory()
                .selectFrom(QPost.post)
                .fetchCount();
    }

    @Override
    public Long count(PostSearch search) {
        JPAQuery query = getQueryFactory().selectFrom(QPost.post);
        BooleanBuilder builder = CriteriaUtils.addSearchConjunction(search, new BooleanBuilder());
        query.where(builder);
        return query.fetchCount();
    }

    @Override
    public List<Post> findScheduledToBePublished() {
        QPost post = QPost.post;
        return getQueryFactory()
                .selectFrom(post)
                .where(post.status.eq(PostStatus.FUTURE), post.publishedAt.before(LocalDateTime.now()))
                .fetch();
    }

    @Override
    public List<Post> findScheduledToBeIndexed(LocalDateTime indicesLastUpdated) {
        QPost post = QPost.post;
        return getQueryFactory()
                .selectFrom(post)
                .where(post.status.eq(PostStatus.PUBLISH), post.modifiedAt.after(indicesLastUpdated))
                .orderBy(post.publishedAt.asc())
                .fetch();
    }

    @Override
    public void disableSplashPosts() {
        QPost post = QPost.post;
        getQueryFactory()
                .update(post)
                .set(post.splash, false)
                .where(post.status.eq(PostStatus.PUBLISH), post.splash.eq(true))
                .execute();
    }

    @Override
    public void disableFeaturedPosts() {
        QPost post = QPost.post;
        getQueryFactory()
                .update(post)
                .set(post.featured, false)
                .where(post.status.eq(PostStatus.PUBLISH), post.featured.eq(true))
                .execute();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    private JPAQuery addProjections(JPAQuery query) {
        QPost post = QPost.post;
        return query.select(
                post.id, post.title, post.bylineName, post.splash, post.featured, post.picked,
                post.category.name, post.creator.displayName, post.modifiedUser.displayName,
                post.createdAt, post.modifiedAt, post.publishedAt, post.bookmarkCount, post.status
        );
    }

    private List<AdminPostSummaryDto> toDtoList(List<Tuple> result) {
        QPost post = QPost.post;
        List<AdminPostSummaryDto> dtoList = new ArrayList<>();
        for (Tuple row : result) {
            AdminPostSummaryDto dto = new AdminPostSummaryDto();
            dto.setId(row.get(post.id));
            dto.setTitle(row.get(post.title));
            dto.setBylineName(row.get(post.bylineName));
            dto.setSplash(row.get(post.splash));
            dto.setFeatured(row.get(post.featured));
            dto.setPicked(row.get(post.picked));
            dto.setCategoryName(row.get(post.category.name));
            dto.setCreatorName(row.get(post.creator.displayName));
            dto.setModifiedUserName(row.get(post.modifiedUser.displayName));
            dto.setCreatedAt(row.get(post.createdAt));
            dto.setModifiedAt(row.get(post.modifiedAt));
            dto.setPublishedAt(row.get(post.publishedAt));
            dto.setBookmarkCount(row.get(post.bookmarkCount));
            dto.setStatus(row.get(post.status).toString());
            dtoList.add(dto);
        }
        return dtoList;
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
