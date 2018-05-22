package com.indiepost.repository.jpa;

import com.indiepost.dto.post.AdminPostSummaryDto;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.repository.AdminPostRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.model.QPost.post;
import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

/**
 * Created by jake on 17. 1. 11.
 */
@Repository
@SuppressWarnings("unchecked")
public class AdminPostRepositoryJpa implements AdminPostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long persist(Post post) {
        if (post.getCategoryId() != null) {
            Category categoryReference = entityManager.getReference(Category.class, post.getCategoryId());
            post.setCategory(categoryReference);
        }
        if (post.getTitleImageId() != null) {
            ImageSet titleImageReference = entityManager.getReference(ImageSet.class, post.getTitleImageId());
            post.setTitleImage(titleImageReference);
        }
        entityManager.persist(post);
        return post.getId();
    }

    @Override
    public Post findOne(Long id) {
        return getQueryFactory()
                .selectFrom(post)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();
    }

    @Override
    public void delete(Post post) {
        entityManager.remove(post);
    }

    @Override
    public void deleteById(Long id) {
        Post post = entityManager.getReference(Post.class, id);
        entityManager.remove(post);
    }

    @Override
    public List<AdminPostSummaryDto> findByIdIn(List<Long> ids) {
        JPAQuery query = getQueryFactory().selectFrom(post);
        addProjections(query)
                .innerJoin(post.author, QUser.user)
                .innerJoin(post.editor, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .distinct();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.id.in(ids));

        query.where(builder);
        List<Tuple> rows = query.fetch();
        if (rows.size() == 0) {
            return Collections.emptyList();
        }
        List<AdminPostSummaryDto> posts = toDtoList(rows);
        return ids.stream().map(id -> {
            for (AdminPostSummaryDto post : posts) {
                if (id.equals(post.getId())) {
                    return post;
                }
            }
            return null;
        }).collect(Collectors.toList());
    }

    @Override
    public List<AdminPostSummaryDto> find(User currentUser, Pageable pageable) {
        return null;
    }

    @Override
    public List<AdminPostSummaryDto> find(User currentUser, PostStatus status, Pageable pageable) {
        JPAQuery query = getQueryFactory().selectFrom(post);
        addProjections(query)
                .innerJoin(post.author, QUser.user)
                .innerJoin(post.editor, QUser.user)
                .innerJoin(post.category, QCategory.category)
                .orderBy(post.publishedAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .distinct();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.status.eq(status));
        addPrivacyCriteria(builder, status, currentUser);

        query.where(builder);
        return toDtoList(query.fetch());
    }

    @Override
    public List<Long> findIds(User currentUser, PostStatus status) {
        JPAQuery query = getQueryFactory().selectFrom(post)
                .select(post.id)
                .innerJoin(post.author, QUser.user)
                .innerJoin(post.editor, QUser.user)
                .distinct();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(post.status.eq(status));
        addPrivacyCriteria(builder, status, currentUser);

        query.where(builder);
        return query.fetch();
    }

    @Override
    public List<String> findAllDisplayNames() {
        return getQueryFactory()
                .from(post)
                .select(post.displayName)
                .where(post.displayName.isNotEmpty())
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

    private JPAQuery addProjections(JPAQuery query) {
        return query.select(
                post.id, post.originalId, post.title, post.displayName, post.splash, post.featured, post.picked,
                post.category.name, post.author.displayName, post.editor.displayName,
                post.createdAt, post.modifiedAt, post.publishedAt, post.status
        );
    }

    private void addPrivacyCriteria(BooleanBuilder builder, PostStatus status, User currentUser) {
        switch (currentUser.getHighestRole()) {
            case Administrator:
                return;
            case EditorInChief:
            case Editor:
                if (status.equals(PostStatus.PUBLISH) ||
                        status.equals(PostStatus.FUTURE) ||
                        status.equals(PostStatus.PENDING)) {
                    return;
                }
                builder.and(post.editorId.eq(currentUser.getId()));
                return;
            default:
                builder.and(post.authorId.eq(currentUser.getId()));
        }
    }

    private List<AdminPostSummaryDto> toDtoList(List<Tuple> result) {
        if (result == null) {
            return new ArrayList<>();
        }
        List<AdminPostSummaryDto> dtoList = new ArrayList<>();
        for (Tuple row : result) {
            AdminPostSummaryDto dto = new AdminPostSummaryDto();
            dto.setId(row.get(post.id));
            dto.setOriginalId(row.get(post.originalId));
            dto.setTitle(row.get(post.title));
            dto.setDisplayName(row.get(post.displayName));
            dto.setSplash(row.get(post.splash));
            dto.setFeatured(row.get(post.featured));
            dto.setPicked(row.get(post.picked));
            dto.setCategoryName(row.get(post.category.name));
            dto.setAuthorDisplayName(row.get(post.author.displayName));
            dto.setEditorDisplayName(row.get(post.editor.displayName));
            dto.setCreatedAt(localDateTimeToInstant(row.get(post.createdAt)));
            dto.setModifiedAt(localDateTimeToInstant(row.get(post.modifiedAt)));
            dto.setPublishedAt(localDateTimeToInstant(row.get(post.publishedAt)));
            dto.setStatus(row.get(post.status).toString());
            List<PostContributor> postContributorsList = row.get(post.postContributors);
            if (postContributorsList != null) {
                List<String> contributors = postContributorsList.stream()
                        .map(postContributor -> postContributor.getContributor().getFullName())
                        .collect(Collectors.toList());
                dto.setContributors(contributors);
            }
            List<PostTag> postTags = row.get(post.postTags);
            if (postTags != null) {
                List<String> tags = postTags.stream()
                        .map(postTag -> postTag.getTag().getName())
                        .collect(Collectors.toList());
                dto.setTags(tags);
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
