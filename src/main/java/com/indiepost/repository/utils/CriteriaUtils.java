package com.indiepost.repository.utils;

import com.indiepost.dto.analytics.PostStatDto;
import com.indiepost.dto.analytics.ShareStat;
import com.indiepost.dto.analytics.TimeDomainDoubleStat;
import com.indiepost.dto.analytics.TimeDomainStat;
import com.indiepost.dto.post.PostQuery;
import com.indiepost.enums.Types;
import com.indiepost.model.QPost;
import com.querydsl.core.BooleanBuilder;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.transform.ResultTransformer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.utils.DateUtil.localDateTimeToDate;

/**
 * Created by jake on 17. 1. 14.
 */
public interface CriteriaUtils {

    static Criteria setPageToCriteria(Criteria criteria, Pageable pageable) {
        Sort sort = pageable.getSort();
        if (sort != null) {
            for (Sort.Order order : sort) {
                if (order.getDirection().equals(Sort.Direction.ASC)) {
                    criteria.addOrder(Order.asc(order.getProperty()));
                } else {
                    criteria.addOrder(Order.desc(order.getProperty()));
                }
            }
        }
        criteria.setFirstResult(pageable.getOffset());
        criteria.setMaxResults(pageable.getPageSize());
        return criteria;
    }

    static BooleanBuilder addSearchConjunction(PostQuery search, BooleanBuilder builder) {
        QPost post = QPost.post;
        if (search.getStatus() != null) {
            builder.and(post.status.eq(search.getStatus()));
        }
        if (search.getCreatorId() != null) {
            builder.and(post.creatorId.eq(search.getCreatorId()));
        }
        if (search.getModifiedUserId() != null) {
            builder.and(post.modifiedUserId.eq(search.getModifiedUserId()));
        }
        if (search.getCategoryId() != null) {
            builder.and(post.categoryId.eq(search.getCategoryId()));
        }
        if (search.getCategorySlug() != null) {
            builder.and(post.category.slug.eq(search.getCategorySlug()));
        }
        if (search.getPublishedAfter() != null) {
            builder.and(post.publishedAt.goe(search.getPublishedAfter()));
        }
        if (search.getPublishedBefore() != null) {
            builder.and(post.publishedAt.loe(search.getPublishedBefore()));
        }
        if (search.getCreatedAfter() != null) {
            builder.and(post.createdAt.goe(search.getCreatedAfter()));
        }
        if (search.getCreatedBefore() != null) {
            builder.and(post.createdAt.loe(search.getCreatedBefore()));
        }
        if (search.getModifiedAfter() != null) {
            builder.and(post.modifiedAt.goe(search.getModifiedAfter()));
        }
        if (search.getModifiedBefore() != null) {
            builder.and(post.modifiedAt.loe(search.getModifiedBefore()));
        }
        builder.and(post.splash.eq(search.isSplash()));
        builder.and(post.featured.eq(search.isFeatured()));
        builder.and(post.picked.eq(search.isPicked()));
        return builder;
    }

    static List<TimeDomainStat> getTrend(Query query, Types.TimeDomainDuration duration, LocalDateTime since, LocalDateTime until) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                if (Types.TimeDomainDuration.HOURLY.equals(duration)) {
                    return new TimeDomainStat(
                            ((Timestamp) tuple[0]).toLocalDateTime(),
                            ((BigInteger) tuple[1]).longValue()
                    );
                }
                return new TimeDomainStat(
                        ((Date) tuple[0]).toLocalDate().atStartOfDay(),
                        ((BigInteger) tuple[1]).longValue()
                );
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        });
        return query.list();
    }

    static List<TimeDomainDoubleStat> getDoubleTrend(Query query, Types.TimeDomainDuration duration, LocalDateTime since, LocalDateTime until) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setResultTransformer(new ResultTransformer() {
            @Override
            public Object transformTuple(Object[] tuple, String[] aliases) {
                if (Types.TimeDomainDuration.HOURLY.equals(duration)) {
                    return new TimeDomainDoubleStat(
                            ((Timestamp) tuple[0]).toLocalDateTime(),
                            ((BigInteger) tuple[1]).longValue(),
                            ((BigInteger) tuple[2]).longValue()
                    );
                }
                return new TimeDomainDoubleStat(
                        ((Date) tuple[0]).toLocalDate().atStartOfDay(),
                        ((BigInteger) tuple[1]).longValue(),
                        ((BigInteger) tuple[2]).longValue()
                );
            }

            @Override
            public List transformList(List collection) {
                return collection;
            }
        });
        return query.list();
    }

    static List<ShareStat> getShare(Query query, LocalDateTime since, LocalDateTime until, Long limit) {
        return getShare(query, since, until, limit, null);
    }

    static List<ShareStat> getShare(Query query, LocalDateTime since, LocalDateTime until, Long limit, String client) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setLong("limit", limit);
        if (client != null) {
            query.setParameter("client", client);
        }
        query.setResultTransformer(new ShareStatResultTransformer());
        return query.list();
    }

    static List<PostStatDto> getPostShare(Query query, LocalDateTime since, LocalDateTime until, Long limit) {
        query.setParameter("since", localDateTimeToDate(since));
        query.setParameter("until", localDateTimeToDate(until));
        query.setLong("limit", limit);
        query.setResultTransformer(new PostStatsResultTransformer());
        return query.list();
    }
}
