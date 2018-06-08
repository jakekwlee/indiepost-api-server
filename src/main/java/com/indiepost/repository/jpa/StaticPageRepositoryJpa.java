package com.indiepost.repository.jpa;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.QStaticPage;
import com.indiepost.model.StaticPage;
import com.indiepost.repository.StaticPageRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

import static com.indiepost.model.QStaticPage.staticPage;
import static com.indiepost.model.QUser.user;
import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

/**
 * Created by jake on 17. 3. 5.
 */
@Repository
public class StaticPageRepositoryJpa implements StaticPageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(StaticPage staticPage) {
        entityManager.persist(staticPage);
        return staticPage.getId();
    }

    @Override
    public StaticPage findById(Long id) {
        return entityManager.find(StaticPage.class, id);
    }

    @Override
    public void update(StaticPage page) {
        entityManager.persist(page);
    }

    @Override
    public void delete(StaticPage page) {
        entityManager.remove(page);
    }

    @Override
    public Page<StaticPageDto> find(Pageable pageable) {
        QStaticPage p = staticPage;
        List<Tuple> rows = getQueryFactory().select(
                p.id,
                p.title,
                p.status,
                p.title,
                p.createdAt,
                p.modifiedAt,
                p.displayOrder,
                p.author.displayName)
                .from(p)
                .innerJoin(user)
                .on(p.authorId.eq(user.id))
                .orderBy(p.displayOrder.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<StaticPageDto> dtoList = toDtoList(rows);
        return new PageImpl<>(dtoList, pageable, count());
    }

    @Override
    public Page<StaticPageDto> find(Pageable pageable, Types.PostStatus pageStatus) {
        QStaticPage p = staticPage;
        List<Tuple> rows = getQueryFactory().select(
                p.id,
                p.title,
                p.status,
                p.title,
                p.createdAt,
                p.modifiedAt,
                p.displayOrder,
                p.slug,
                p.author.displayName)
                .from(p)
                .innerJoin(user)
                .on(p.authorId.eq(user.id))
                .where(p.status.eq(pageStatus))
                .orderBy(p.displayOrder.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<StaticPageDto> dtoList = toDtoList(rows);
        Long count = count(pageStatus);
        return new PageImpl<>(dtoList, pageable, count);
    }

    @Override
    public Long count() {
        return getQueryFactory().selectFrom(staticPage).fetchCount();
    }

    @Override
    public Long count(Types.PostStatus pageStatus) {
        return getQueryFactory()
                .selectFrom(staticPage)
                .where(staticPage.status.eq(pageStatus))
                .fetchCount();
    }

    @Override
    public StaticPage findBySlug(String slug) {
        return getQueryFactory()
                .selectFrom(staticPage)
                .where(staticPage.slug.eq(slug))
                .fetchOne();
    }

    @Override
    public void bulkUpdateStatusByIds(List<Long> ids, Types.PostStatus status) {
        getQueryFactory()
                .update(staticPage)
                .set(staticPage.status, status)
                .where(staticPage.id.in(ids))
                .execute();
    }

    @Override
    public void bulkDeleteByIds(List<Long> ids) {
        getQueryFactory()
                .delete(staticPage)
                .where(staticPage.id.in(ids))
                .execute();
    }

    @Override
    public void bulkDeleteByStatus(Types.PostStatus status) {
        if (!status.equals(Types.PostStatus.TRASH)) {
            // TODO error handling
            return;
        }
        getQueryFactory()
                .delete(staticPage)
                .where(staticPage.status.eq(status))
                .execute();
    }

    @Override
    public boolean isExists(Long id) {
        return getQueryFactory()
                .selectFrom(staticPage)
                .where(staticPage.id.eq(id))
                .fetchCount() > 1;
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    private List<StaticPageDto> toDtoList(List<Tuple> result) {
        return result.stream().map(row -> {
            StaticPageDto dto = new StaticPageDto();
            dto.setId(row.get(staticPage.id));
            dto.setTitle(row.get(staticPage.title));
            dto.setAuthorDisplayName(row.get(staticPage.author.displayName));
            dto.setCreatedAt(localDateTimeToInstant(row.get(staticPage.createdAt)));
            dto.setModifiedAt(localDateTimeToInstant(row.get(staticPage.modifiedAt)));
            dto.setDisplayOrder(row.get(staticPage.displayOrder));
            dto.setSlug(row.get(staticPage.slug));
            return dto;
        }).collect(Collectors.toList());
    }
}
