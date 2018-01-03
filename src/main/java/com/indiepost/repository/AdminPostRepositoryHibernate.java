package com.indiepost.repository;

import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.model.analytics.QPageview;
import com.indiepost.model.legacy.QLegacyPost;
import com.indiepost.model.legacy.QLegacyPostContent;
import com.indiepost.repository.utils.CriteriaUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.enums.Types.isPublicStatus;
import static com.indiepost.model.QPost.post;

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
        entityManager.persist(post);
        entityManager.flush();
        return post.getId();
    }

    @Override
    public Post findOne(Long id) {
        return entityManager.find(Post.class, id);
    }

    @Override
    public void delete(Post post) {
        entityManager.remove(post);
    }

    @Override
    public void deleteById(Long id) {
        Post post = findOne(id);
        delete(post);
    }

    @Override
    public void bulkDeleteByUserAndStatus(User currentUser, PostStatus status) {
        if (isPublicStatus(status)) {
            // Directly bulk delete public status post is prohibited. PUBLIC -> TRASH -> DELETED -> gone
            return;
        }
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.status.eq(status));
        Types.UserRole highestRole = currentUser.getHighestRole();

        if (!highestRole.equals(Types.UserRole.Administrator)) {
            builder.and(post.modifiedUserId.eq(currentUser.getId()));
        }

        JPAQueryFactory queryFactory = getQueryFactory();
        List<Long> postIds = queryFactory
                .select(post.id)
                .from(post)
                .where(builder)
                .fetch();

        if (postIds == null || postIds.isEmpty()) {
            // if no matched posts, do nothing
            return;
        }
        QPageview pageview = QPageview.pageview;

        // if statistics data exist, set ref key to null
        queryFactory
                .update(pageview)
                .where(pageview.postId.in(postIds))
                .setNull(pageview.postId)
                .execute();

        builder.and(post.legacyPostId.isNotNull());
        List<Long> legacyPostIds = queryFactory.select(post.legacyPostId).from(post).where(builder).fetch();

        QCachedPostStat cachedPostStat = QCachedPostStat.cachedPostStat;
        QLegacyStats legacyStats = QLegacyStats.legacyStats;
        QPostTag postTag = QPostTag.postTag;
        QPostContributor postContributor = QPostContributor.postContributor;
        QBookmark bookmark = QBookmark.bookmark;

        queryFactory.delete(cachedPostStat).where(cachedPostStat.post.id.in(postIds)).execute();
        queryFactory.delete(legacyStats).where(legacyStats.post.id.in(postIds)).execute();
        queryFactory.delete(postTag).where(postTag.post.id.in(postIds)).execute();
        queryFactory.delete(postContributor).where(postContributor.post.id.in(postIds)).execute();
        queryFactory.delete(bookmark).where(bookmark.post.id.in(postIds)).execute();

        // delete posts finally!
        queryFactory.delete(post).where(post.id.in(postIds)).execute();

        if (legacyPostIds != null && !legacyPostIds.isEmpty()) {
            QLegacyPost legacyPost = QLegacyPost.legacyPost;
            QLegacyPostContent legacyPostContent = QLegacyPostContent.legacyPostContent;
            queryFactory.delete(legacyPostContent).where(legacyPostContent.legacyPost.no.in(legacyPostIds)).execute();
            queryFactory.delete(legacyPost).where(legacyPost.no.in(legacyPostIds)).execute();
        }
    }

    @Override
    public List<AdminPostSummaryDto> findByIdIn(List<Long> ids) {
        JPAQuery query = getQueryFactory().selectFrom(post);
        query
                .innerJoin(post.creator, QUser.user)
                .innerJoin(post.modifiedUser, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .orderBy(post.publishedAt.desc())
                .distinct();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.id.in(ids));

        query.where(builder);
        return postToDtoList(query.fetch());
    }

    @Override
    public List<AdminPostSummaryDto> find(User currentUser, Pageable pageable) {
        return null;
    }

    @Override
    public List<AdminPostSummaryDto> find(User currentUser, PostStatus status, Pageable pageable) {
        JPAQuery query = getQueryFactory().selectFrom(post);
        query
                .innerJoin(post.creator, QUser.user)
                .innerJoin(post.modifiedUser, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .orderBy(post.publishedAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .distinct();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.status.eq(status));
        addPrivacyCriteria(builder, status, currentUser);

        query.where(builder);
        return postToDtoList(query.fetch());
    }

    @Override
    public List<String> findAllDisplayNames() {
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
                .selectFrom(post)
                .fetchCount();
    }

    @Override
    public Long count(PostQuery search) {
        JPAQuery query = getQueryFactory().selectFrom(post);
        BooleanBuilder builder = CriteriaUtils.addSearchConjunction(search, new BooleanBuilder());
        query.where(builder);
        return query.fetchCount();
    }

    @Override
    public Long count(PostStatus status, User currentUser) {
        JPAQuery query = getQueryFactory().from(post);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.status.eq(status));
        addPrivacyCriteria(builder, status, currentUser);

        query.where(builder);
        return query.fetchCount();
    }

    @Override
    public List<Post> findScheduledToBePublished() {
        return getQueryFactory()
                .selectFrom(post)
                .where(post.status.eq(PostStatus.FUTURE), post.publishedAt.before(LocalDateTime.now()))
                .fetch();
    }

    @Override
    public List<Post> findScheduledToBeIndexed(LocalDateTime indicesLastUpdated) {
        return getQueryFactory()
                .selectFrom(post)
                .where(post.modifiedAt.after(indicesLastUpdated))
                .orderBy(post.id.asc())
                .fetch();
    }

    @Override
    public void disableSplashPosts() {
        getQueryFactory()
                .update(post)
                .set(post.splash, false)
                .where(post.status.eq(PostStatus.PUBLISH), post.splash.eq(true))
                .execute();
    }

    @Override
    public void disableFeaturedPosts() {
        getQueryFactory()
                .update(post)
                .set(post.featured, false)
                .where(post.status.eq(PostStatus.PUBLISH), post.featured.eq(true))
                .execute();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    private JPAQuery addProjections(JPAQuery query) {
        return query.select(
                post.id, post.title, post.bylineName, post.splash, post.featured, post.picked,
                post.category.name, post.creator.displayName, post.modifiedUser.displayName,
                post.createdAt, post.modifiedAt, post.publishedAt, post.bookmarkCount, post.status
        );
    }

    private BooleanBuilder addPrivacyCriteria(BooleanBuilder builder, PostStatus status, User currentUser) {
        switch (currentUser.getHighestRole()) {
            case Administrator:
                return builder;
            case EditorInChief:
            case Editor:
                if (status.equals(PostStatus.PUBLISH) ||
                        status.equals(PostStatus.FUTURE) ||
                        status.equals(PostStatus.PENDING)) {
                    return builder;
                }
                builder.and(post.modifiedUserId.eq(currentUser.getId()));
                return builder;
            default:
                builder.and(post.creatorId.eq(currentUser.getId()));
                return builder;
        }
    }

    private List<AdminPostSummaryDto> postToDtoList(List<Post> posts) {
        if (posts == null) {
            return new ArrayList<>();
        }
        List<AdminPostSummaryDto> dtoList = new ArrayList<>();
        for (Post post : posts) {
            AdminPostSummaryDto dto = new AdminPostSummaryDto();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setBylineName(post.getBylineName());
            dto.setSplash(post.isSplash());
            dto.setFeatured(post.isFeatured());
            dto.setPicked(post.isPicked());
            dto.setCategoryName(post.getCategory().getName());
            dto.setCreatorName(post.getCreator().getDisplayName());
            dto.setModifiedUserName(post.getModifiedUser().getDisplayName());
            dto.setCreatedAt(post.getCreatedAt());
            dto.setModifiedAt(post.getModifiedAt());
            dto.setPublishedAt(post.getPublishedAt());
            dto.setBookmarkCount(post.getBookmarkCount());
            dto.setStatus(post.getStatus().toString());
            dto.setContributors(post.getContributors().stream().map(contributor -> contributor.getName()).collect(Collectors.toList()));
            dto.setTags(post.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList()));
            dtoList.add(dto);
        }
        return dtoList;
    }

    private List<AdminPostSummaryDto> toDtoList(List<Tuple> result) {
        if (result == null) {
            return new ArrayList<>();
        }
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
//            List<PostContributor> postContributorsList = row.get(post.postContributors);
//            if (postContributorsList != null) {
//                List<String> contributors = postContributorsList.stream()
//                        .map(postContributor -> postContributor.getContributor().getName())
//                        .collect(Collectors.toList());
//                dto.setContributors(contributors);
//            }
//            List<PostTag> postTags = row.get(post.postTags);
//            if (postTags != null) {
//                List<String> tags = postTags.stream()
//                        .map(postTag -> postTag.getTag().getName())
//                        .collect(Collectors.toList());
//                dto.setTags(tags);
//            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
