package com.indiepost.repository.jpa;

import com.indiepost.model.analytics.Campaign;
import com.indiepost.model.analytics.QCampaign;
import com.indiepost.model.analytics.QClick;
import com.indiepost.model.analytics.QLink;
import com.indiepost.repository.CampaignRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class CampaignRepositoryJpa implements CampaignRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QCampaign qCampaign = QCampaign.campaign;

    private QLink qLink = QLink.link;

    private QClick qClick = QClick.click;

    @Override
    public void save(Campaign campaign) {
        entityManager.persist(campaign);
    }

    @Override
    public Optional<Campaign> findOne(Long id) {
        Campaign campaign = entityManager.find(Campaign.class, id);
        if (campaign != null) {
            return Optional.of(campaign);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(Long id) {
        Campaign campaignReference = entityManager.getReference(Campaign.class, id);
        entityManager.remove(campaignReference);
    }

    @Override
    public Long count() {
        return getQueryFactory()
                .selectFrom(qCampaign)
                .fetchCount();
    }

    @Override
    public Page<Campaign> find(Pageable pageable) {
        List<Campaign> campaignList = getQueryFactory()
                .selectFrom(qCampaign)
                .orderBy(qCampaign.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        Long count = count();

        return new PageImpl<>(campaignList, pageable, count);
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
