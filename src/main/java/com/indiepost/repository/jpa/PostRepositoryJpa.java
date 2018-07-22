package com.indiepost.repository.jpa;

import com.indiepost.dto.Timeline;
import com.indiepost.dto.TimelineRequest;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.indiepost.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.indiepost.model.QPost.post;
import static com.indiepost.repository.utils.CriteriaUtils.addConjunction;
import static com.indiepost.utils.DateUtil.instantToLocalDateTime;
import static com.indiepost.utils.DateUtil.localDateTimeToInstant;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostRepositoryJpa implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post findById(Long id) {
        return getQueryFactory()
                .selectFrom(post)
                .join(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .fetchJoin()
                .where(post.id.eq(id))
                .fetchOne();
    }

    @Override
    public Long count() {
        return getQueryFactory()
                .selectFrom(post)
                .fetchCount();
    }

    @Override
    public Long count(PostQuery search) {
        BooleanBuilder builder = addConjunction(search, new BooleanBuilder());
        return getQueryFactory()
                .selectFrom(post)
                .where(builder)
                .fetchCount();
    }

    @Override
    public List<PostSummaryDto> findByIds(List<Long> ids) {
        if (isEmpty(ids)) {
            return new ArrayList<>();
        }
        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(post.id.in(ids))
                .distinct();
        List<Tuple> rows = query.fetch();
        List<Tuple> result = new ArrayList<>();
        for (Long id : ids) {
            for (Tuple row : rows) {
                Long postId = row.get(post.id);
                if (id.equals(postId)) {
                    result.add(row);
                    break;
                }
            }
        }
        return toDtoList(result);
    }

    @Override
    public Page<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable) {
        PostQuery query = new PostQuery.Builder(PostStatus.PUBLISH)
                .category(slug)
                .build();
        return this.query(query, pageable);
    }

    @Override
    public Page<PostSummaryDto> findByTagName(String tagName, Pageable pageable) {
        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .innerJoin(post.postTags, QPostTag.postTag)
                .innerJoin(QPostTag.postTag.tag, QTag.tag)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(QTag.tag.name.eq(tagName).and(post.status.eq(PostStatus.PUBLISH)))
                .orderBy(post.publishedAt.desc(), QPostTag.postTag.priority.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).distinct();
        List<Tuple> result = query.fetch();
        if (result.size() == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Long total = getQueryFactory().selectFrom(post)
                .innerJoin(post.postTags, QPostTag.postTag)
                .innerJoin(QPostTag.postTag.tag, QTag.tag)
                .where(QTag.tag.name.eq(tagName).and(post.status.eq(PostStatus.PUBLISH)))
                .fetchCount();
        List<PostSummaryDto> dtoList = toDtoList(result);
        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public Page<PostSummaryDto> findByContributorFullName(String fullName, Pageable pageable) {
        QCategory ct = QCategory.category;
        QPostContributor pc = QPostContributor.postContributor;
        QContributor c = QContributor.contributor;
        QImageSet i = QImageSet.imageSet;

        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, ct)
                .innerJoin(post.postContributors, pc)
                .innerJoin(pc.contributor, c)
                .leftJoin(post.titleImage, i)
                .where(c.fullName.eq(fullName).and(post.status.eq(PostStatus.PUBLISH)))
                .orderBy(post.publishedAt.desc(), QPostContributor.postContributor.priority.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).distinct();
        List<Tuple> result = query.fetch();
        if (result.size() == 0) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        Long total = getQueryFactory().selectFrom(post)
                .innerJoin(post.postContributors, pc)
                .innerJoin(pc.contributor, c)
                .where(c.fullName.eq(fullName).and(post.status.eq(PostStatus.PUBLISH)))
                .fetchCount();
        List<PostSummaryDto> dtoList = toDtoList(result);
        return new PageImpl<>(dtoList, pageable, total);
    }

    @Override
    public Page<PostSummaryDto> findByStatus(PostStatus status, Pageable pageable) {
        PostQuery query = new PostQuery.Builder(status).build();
        return this.query(query, pageable);
    }

    @Override
    public List<PostSummaryDto> findScheduledPosts() {
        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(post.status.eq(PostStatus.FUTURE))
                .orderBy(post.publishedAt.asc())
                .distinct();
        List<Tuple> result = query.fetch();
        return toDtoList(result);
    }

    @Override
    public Page<PostSummaryDto> query(PostQuery postQuery, Pageable pageable) {
        JPAQuery query = getQueryFactory().from(post);
        BooleanBuilder builder = addConjunction(postQuery, new BooleanBuilder());
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(builder)
                .orderBy(post.publishedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).distinct();
        List<Tuple> result = query.fetch();
        Long total = count(postQuery);
        List<PostSummaryDto> dtoList = toDtoList(result);
        return new PageImpl<>(dtoList, pageable, total);

    }

    @Override
    public PostStatus getStatusById(Long postId) {
        return getQueryFactory()
                .select(post.status)
                .from(post)
                .where(post.id.eq(postId))
                .fetchOne();
    }

    @Override
    public Timeline<PostSummaryDto> findReadingHistoryByUserId(Long userId, TimelineRequest request) {
        QCategory ct = QCategory.category;
        QImageSet im = QImageSet.imageSet;
        QPostReading r = QPostReading.postReading;

        BooleanExpression whereClause;
        OrderSpecifier orderClause;
        boolean isAfter = request.isAfter();
        Instant instant = Instant.ofEpochSecond(request.getTimepoint());
        LocalDateTime timepoint = instantToLocalDateTime(instant);

        whereClause = post.status.eq(PostStatus.PUBLISH)
                .and(r.visible.isTrue())
                .and(r.userId.eq(userId))
                .and(isAfter ?
                        r.lastRead.after(timepoint) :
                        r.lastRead.before(timepoint)
                );
        orderClause = r.lastRead.desc();

        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, ct)
                .innerJoin(post.postReadings, r)
                .innerJoin(r.post, post)
                .leftJoin(post.titleImage, im)
                .where(whereClause)
                .orderBy(orderClause)
                .limit(request.getSize()).distinct();
        List<Tuple> result = query.fetch();
        if (result.size() == 0) {
            return new Timeline<>(Collections.emptyList(), request, 0);
        }
        List<PostSummaryDto> dtoList = toDtoList(result);
        Long total = getQueryFactory().selectFrom(post)
                .innerJoin(post.postReadings, r)
                .innerJoin(r.post, post)
                .where(whereClause)
                .fetchCount();
        return new Timeline<>(dtoList, request, total.intValue());
    }

    @Override
    public Timeline<PostSummaryDto> findBookmarksByUserId(Long userId, TimelineRequest request) {
        QCategory ct = QCategory.category;
        QImageSet im = QImageSet.imageSet;
        QBookmark b = QBookmark.bookmark;

        BooleanExpression whereClause;
        OrderSpecifier orderClause;
        boolean isAfter = request.isAfter();
        Instant instant = Instant.ofEpochSecond(request.getTimepoint());
        LocalDateTime timepoint = instantToLocalDateTime(instant);

        whereClause = post.status.eq(PostStatus.PUBLISH)
                .and(b.userId.eq(userId))
                .and(isAfter ?
                        b.created.after(timepoint) :
                        b.created.before(timepoint)
                );
        orderClause = b.created.desc();

        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, ct)
                .innerJoin(post.postBookmarks, b)
                .innerJoin(b.post, post)
                .leftJoin(post.titleImage, im)
                .where(whereClause)
                .orderBy(orderClause)
                .limit(request.getSize()).distinct();
        List<Tuple> result = query.fetch();
        if (result.size() == 0) {
            return new Timeline<>(Collections.emptyList(), request, 0);
        }
        List<PostSummaryDto> dtoList = toDtoList(result);
        Long total = getQueryFactory().selectFrom(post)
                .innerJoin(post.postBookmarks, b)
                .innerJoin(b.post, post)
                .where(whereClause)
                .fetchCount();
        return new Timeline<>(dtoList, request, total.intValue());
    }

    private JPAQuery addProjections(JPAQuery query) {
        return query.select(
                post.id, post.categoryId, post.category.name, post.category.slug, post.splash, post.picked, post.featured,
                post.displayName, post.title, post.publishedAt, post.modifiedAt, post.excerpt, post.titleImage, post.showLastUpdated);
    }


    private List<PostSummaryDto> toDtoList(List<Tuple> result) {
        List<PostSummaryDto> dtoList = new ArrayList<>();
        for (Tuple row : result) {
            PostSummaryDto dto = new PostSummaryDto();
            dto.setId(row.get(post.id));
            dto.setTitle(row.get(post.title));
            dto.setDisplayName(row.get(post.displayName));
            dto.setPublishedAt(localDateTimeToInstant(row.get(post.publishedAt)));
            dto.setModifiedAt(localDateTimeToInstant(row.get(post.modifiedAt)));
            dto.setExcerpt(row.get(post.excerpt));
            dto.setSplash(row.get(post.splash));
            dto.setFeatured(row.get(post.featured));
            dto.setPicked(row.get(post.picked));
            dto.setShowLastUpdated(row.get(post.showLastUpdated));
            dto.setCategoryName(row.get(post.category.slug));
            ImageSet titleImage = row.get(post.titleImage);
            if (titleImage != null) {
                dto.setTitleImage(titleImage);
            }
            dtoList.add(dto);
        }
        return dtoList;
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}