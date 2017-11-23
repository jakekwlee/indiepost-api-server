package com.indiepost.repository;

import com.indiepost.dto.ImageSetDto;
import com.indiepost.dto.post.PostSearch;
import com.indiepost.dto.post.PostSummaryDto;
import com.indiepost.enums.Types.PostStatus;
import com.indiepost.model.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static com.indiepost.repository.utils.CriteriaUtils.addSearchConjunction;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * Created by jake on 7/30/16.
 */
@Repository
@SuppressWarnings("unchecked")
public class PostRepositoryHibernate implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Post findById(Long id) {
        return entityManager.find(Post.class, id);
    }

    @Override
    public Post findByLegacyId(Long id) {
        QPost post = QPost.post;
        return getQueryFactory()
                .selectFrom(post)
                .where(post.legacyPostId.eq(id))
                .fetchOne();
    }

    @Override
    public Long findIdByLegacyId(Long legacyId) {
        QPost post = QPost.post;
        Tuple tuple = getQueryFactory()
                .select(post.id, post.legacyPostId)
                .from(post)
                .where(post.legacyPostId.eq(legacyId))
                .fetchOne();
        return tuple != null ? tuple.get(post.id) : null;
    }

    @Override
    public Long count() {
        return getQueryFactory()
                .selectFrom(QPost.post)
                .fetchCount();
    }

    @Override
    public Long count(PostSearch search) {
        BooleanBuilder builder = addSearchConjunction(search, new BooleanBuilder());
        return getQueryFactory()
                .selectFrom(QPost.post)
                .where(builder)
                .fetchCount();
    }

    @Override
    public List<PostSummaryDto> findByIds(List<Long> ids) {
        if (isNotEmpty(ids)) {
            return new ArrayList<>();
        }
        QPost post = QPost.post;
        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(post.id.in(ids))
                .orderBy(post.publishedAt.desc())
                .distinct();
        List<Tuple> result = query.fetch();
        return toDtoList(result);
    }

    @Override
    public List<PostSummaryDto> findByCategoryId(Long categoryId, Pageable pageable) {
        PostSearch query = new PostSearch();
        query.setCategoryId(categoryId);
        return this.search(query, pageable);
    }

    @Override
    public List<PostSummaryDto> findByCategorySlug(String slug, Pageable pageable) {
        PostSearch query = new PostSearch();
        query.setCategorySlug(slug);
        return this.search(query, pageable);
    }

    @Override
    public List<PostSummaryDto> findByTagName(String tagName, Pageable pageable) {
        QPost post = QPost.post;
        QPostTag postTag = QPostTag.postTag;
        JPAQuery query = getQueryFactory().from(post);
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .innerJoin(post.postTags, postTag)
                .innerJoin(postTag.tag, QTag.tag)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(postTag.tag.name.eq(tagName), post.status.eq(PostStatus.PUBLISH))
                .orderBy(post.publishedAt.desc())
                .distinct();
        List<Tuple> result = query.fetch();
        return toDtoList(result);
    }

    @Override
    public List<PostSummaryDto> findByStatus(PostStatus status, Pageable pageable) {
        PostSearch query = new PostSearch();
        query.setStatus(status);
        return this.search(query, pageable);
    }

    @Override
    public List<PostSummaryDto> findScheduledPosts() {
        QPost post = QPost.post;
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
    public List<Post> search(String text, Pageable pageable) {
        return null;
    }

    @Override
    public List<PostSummaryDto> search(PostSearch search, Pageable pageable) {
        QPost post = QPost.post;
        JPAQuery query = getQueryFactory().from(post);
        BooleanBuilder builder = addSearchConjunction(search, new BooleanBuilder());
        addProjections(query)
                .innerJoin(post.category, QCategory.category)
                .leftJoin(post.titleImage, QImageSet.imageSet)
                .where(builder)
                .orderBy(post.publishedAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).distinct();
        List<Tuple> result = query.fetch();
        return toDtoList(result);
    }

    private JPAQuery addProjections(JPAQuery query) {
        QPost post = QPost.post;
        return query.select(
                post.id, post.legacyPostId, post.category.slug, post.splash, post.picked, post.featured,
                post.bylineName, post.title, post.publishedAt, post.excerpt, post.titleImage,
                post.bookmarkCount);
    }


    private List<PostSummaryDto> toDtoList(List<Tuple> result) {
        QPost post = QPost.post;
        List<PostSummaryDto> dtoList = new ArrayList<>();
        for (Tuple row : result) {
            PostSummaryDto dto = new PostSummaryDto();
            dto.setId(row.get(post.id));
            dto.setLegacyPostId(row.get(post.legacyPostId));
            dto.setTitle(row.get(post.title));
            dto.setBylineName(row.get(post.bylineName));
            dto.setPublishedAt(row.get(post.publishedAt));
            dto.setExcerpt(row.get(post.excerpt));
            dto.setSplash(row.get(post.splash));
            dto.setFeatured(row.get(post.featured));
            dto.setPicked(row.get(post.picked));
            dto.setCategory(row.get(post.category.slug));
            ImageSet titleImage = row.get(post.titleImage);
            if (titleImage != null) {
                ImageSetDto imageSet = new ImageSetDto();
                imageSet.setId(titleImage.getId());
                if (titleImage.getOriginal() != null) {
                    imageSet.setOriginal(titleImage.getOriginal().getFilePath());
                }
                if (titleImage.getLarge() != null) {
                    imageSet.setLarge(titleImage.getLarge().getFilePath());
                }
                if (titleImage.getOptimized() != null) {
                    imageSet.setOptimized(titleImage.getOptimized().getFilePath());
                }
                if (titleImage.getSmall() != null) {
                    imageSet.setSmall(titleImage.getSmall().getFilePath());
                }
                if (titleImage.getThumbnail() != null) {
                    imageSet.setThumbnail(titleImage.getThumbnail().getFilePath());
                }
                dto.setTitleImage(imageSet);
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}