package com.indiepost.repository.jpa;

import com.indiepost.model.analytics.Click;
import com.indiepost.model.analytics.QClick;
import com.indiepost.repository.ClickRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ClickRepositoryJpa implements ClickRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QClick qClick = QClick.click;

    @Override
    public void save(Click click) {
        entityManager.persist(click);
    }

    @Override
    public Long countByLinkId(Long linkId) {
        return getQueryFactory()
                .selectFrom(qClick)
                .where(qClick.link.id.eq(linkId))
                .fetchCount();
    }

    @Override
    public Long countValidClicksByLinkId(Long linkId) {
        return getQueryFactory()
                .selectFrom(qClick)
                .innerJoin(qClick.link)
                .innerJoin(qClick.link.campaign)
                .where(qClick.link.id.eq(linkId)
                        .and(qClick.timestamp.between(
                                qClick.link.campaign.startAt, qClick.link.campaign.endAt
                                )
                        )
                ).fetchCount();
    }

    @Override
    public Long countAllClicksByCampaignId(Long campaignId) {
        return getQueryFactory()
                .selectDistinct(qClick.visitorId)
                .from(qClick)
                .innerJoin(qClick.link)
                .where(qClick.link.campaignId.eq(campaignId))
                .fetchCount();
    }

    @Override
    public Long countValidClicksByCampaignId(Long campaignId) {
        return getQueryFactory()
                .selectDistinct(qClick.visitorId)
                .from(qClick)
                .innerJoin(qClick.link)
                .innerJoin(qClick.link.campaign)
                .where(qClick.link.campaignId.eq(campaignId).and(
                        qClick.timestamp.between(qClick.link.campaign.startAt, qClick.link.campaign.endAt)
                ))
                .fetchCount();
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
