package com.indiepost.repository.jpa;

import com.indiepost.dto.stat.LinkDto;
import com.indiepost.model.analytics.Campaign;
import com.indiepost.model.analytics.QCampaign;
import com.indiepost.repository.CampaignRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CampaignRepositoryJpa implements CampaignRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private QCampaign qCampaign = QCampaign.campaign;

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

    @Override
    public List<LinkDto> findCampaignLinksOrderByClicks(Long campaignId) {
        Query query = getNamedQuery("@GET_LINKS_BY_CAMPAIGN_ID_ORDER_BY_CLICKS");
        query.setParameter("id", campaignId);
        List<Object[]> result = query.getResultList();
        return result.stream()
                .map(objects -> {
                    Long id = ((BigInteger) objects[0]).longValue();
                    Long cid = ((BigInteger) objects[1]).longValue();
                    String name = (String) objects[2];
                    String uid = (String) objects[3];
                    String url = (String) objects[4];
                    LocalDateTime createdAt = ((Timestamp) objects[5]).toLocalDateTime();
                    Integer validClicks = ((BigInteger) objects[6]).intValue();

                    LinkDto dto = new LinkDto();
                    dto.setId(id);
                    dto.setCampaignId(cid);
                    dto.setName(name);
                    dto.setUid(uid);
                    dto.setUrl(url);
                    dto.setCreatedAt(createdAt);
                    dto.setValidClicks(validClicks.longValue());
                    return dto;
                }).collect(Collectors.toList());
    }

    private JPAQueryFactory getQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    private Query getNamedQuery(String queryName) {
        return entityManager.unwrap(Session.class).getNamedQuery(queryName);
    }
}
