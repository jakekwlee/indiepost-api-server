package com.indiepost.repository.jpa

import com.indiepost.model.analytics.Click
import com.indiepost.model.analytics.QClick
import com.indiepost.model.analytics.Visitor
import com.indiepost.repository.ClickRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ClickRepositoryJpa : ClickRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val qClick = QClick.click

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun save(click: Click) {
        if (click.visitorId != null) {
            val id = click.visitorId
            val ref = entityManager.getReference(Visitor::class.java, id)
            click.visitor = ref
        }
        entityManager.persist(click)
    }

    override fun countByLinkId(linkId: Long): Long {
        return queryFactory
                .selectFrom(qClick)
                .where(qClick.link.id.eq(linkId))
                .fetchCount()
    }

    override fun countValidClicksByLinkId(linkId: Long): Long {
        return queryFactory
                .selectFrom(qClick)
                .innerJoin(qClick.link)
                .innerJoin(qClick.link.campaign)
                .where(qClick.link.id.eq(linkId)
                        .and(qClick.timestamp.between(
                                qClick.link.campaign.startAt, qClick.link.campaign.endAt
                        )
                        )
                ).fetchCount()
    }

    override fun countAllClicksByCampaignId(campaignId: Long): Long {
        return queryFactory
                .selectDistinct(qClick.visitorId)
                .from(qClick)
                .innerJoin(qClick.link)
                .where(qClick.link.campaignId.eq(campaignId))
                .fetchCount()
    }

    override fun countValidClicksByCampaignId(campaignId: Long): Long {
        return queryFactory
                .selectDistinct(qClick.visitorId)
                .from(qClick)
                .innerJoin(qClick.link)
                .innerJoin(qClick.link.campaign)
                .where(qClick.link.campaignId.eq(campaignId).and(
                        qClick.timestamp.between(qClick.link.campaign.startAt, qClick.link.campaign.endAt)
                ))
                .fetchCount()
    }

}
