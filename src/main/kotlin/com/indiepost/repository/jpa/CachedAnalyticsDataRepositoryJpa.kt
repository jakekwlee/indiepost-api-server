package com.indiepost.repository.jpa

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.indiepost.dto.analytics.OverviewStats
import com.indiepost.dto.analytics.PostStatsDto
import com.indiepost.dto.analytics.TopStats
import com.indiepost.enums.Types
import com.indiepost.model.CachedAnalyticsData
import com.indiepost.model.QCachedAnalyticsData
import com.indiepost.repository.CachedAnalyticsDataRepository
import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

//@Repository
class CachedAnalyticsDataRepositoryJpa : CachedAnalyticsDataRepository {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val queryFactory: JPAQueryFactory
        get() = JPAQueryFactory(entityManager)

    override fun <T> find(startDate: LocalDate,
                          endDate: LocalDate,
                          statsType: Types.CachedStatsType,
                          filterType: Types.CachedStatsFilterType,
                          filterValue: String?): T? {
        if (filterType != Types.CachedStatsFilterType.NoFilter)
            return null

        val d = QCachedAnalyticsData.cachedAnalyticsData
        val result = queryFactory.select(d.serializedData)
                .from(d)
                .where(d.filterType.eq(filterType)
                        .and(d.startDate.eq(startDate))
                        .and(d.endDate.eq(endDate))
                        .and(d.statsType.eq(statsType))
                ).fetch()
        val serializedData = result.firstOrNull() ?: return null
        return when (statsType) {
            Types.CachedStatsType.Overview -> objectMapper.readValue(serializedData, OverviewStats::class.java) as T
            Types.CachedStatsType.Tops -> objectMapper.readValue(serializedData, TopStats::class.java) as T
            Types.CachedStatsType.Posts -> objectMapper.readValue(serializedData, PostStatsDto::class.java) as T
        }
    }

    override fun save(stats: CachedAnalyticsData) {
        entityManager.persist(stats)
    }

    companion object {
        val objectMapper: ObjectMapper = ObjectMapper()
                .registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}