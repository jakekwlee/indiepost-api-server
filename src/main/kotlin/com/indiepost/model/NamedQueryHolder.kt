package com.indiepost.model

import javax.persistence.*

/**
 * Created by jake on 8/15/17.
 */
@Entity
@NamedNativeQueries(
        NamedNativeQuery(
                name = "@GET_TOTAL_UNIQUE_PAGEVIEWS",
                query = """select count(distinct s.path, v.id) from Stats s inner join Visitors v on s.visitorId = v.id and v.isAdVisitor is false where s.timestamp between :since and :until"""),
        NamedNativeQuery(
                name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_BY_CLIENT",
                query = """select count(distinct s.path, v.id) from Stats s inner join Visitors v on s.visitorId = v.id where s.timestamp between :since and :until and v.appName = :client"""),
        NamedNativeQuery(
                name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS",
                query = """select count(distinct s.postId, v.id) from Stats s inner join Visitors v on s.visitorId = v.id and v.isAdVisitor is false where s.timestamp between :since and :until and s.postId is not null"""),
        NamedNativeQuery(
                name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS_BY_CLIENT",
                query = """select count(distinct s.postId, v.id) from Stats s inner join Visitors v on s.visitorId = v.id where s.timestamp between :since and :until and v.appName = :client and s.postId is not null"""),
        NamedNativeQuery(
                name = "@GET_PAGEVIEW_TREND_HOURLY",
                query = """select date_add(date(s.timestamp), interval hour(s.timestamp) hour) as statDateTime, count(*) as statValue from Stats s where s.timestamp between :since and :until and s.class <> 'Click' group by statDateTime order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_PAGEVIEW_TREND_DAILY",
                query = """select date(s.timestamp) as statDateTime, count(*) as statValue from Stats s where s.timestamp between :since and :until and s.class <> 'Click' group by statDateTime order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_PAGEVIEW_TREND_MONTHLY",
                query = """select date_sub(date(s.timestamp), interval day(s.timestamp) - 1 day) as statDateTime, count(*) as statValue  from Stats s where s.timestamp between :since and :until and s.class <> 'Click' group by statDateTime """),
        NamedNativeQuery(
                name = "@GET_PAGEVIEW_TREND_YEARLY",
                query = """select makedate(year(s.timestamp), 1) as statDateTime, count(*) as statValue from Stats s where s.timestamp between :since and :until and s.class <> 'Click' group by statDateTime order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_HOURLY",
                query = """select date_add(date(s.timestamp), interval hour(s.timestamp) hour) as dateTime, COUNT(*) as pageviews, COUNT(IF(DATE_ADD(p.publishedAt, interval 10 day) > s.timestamp, true, null)) as recentPageviews from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until and s.class <> 'Click' group by dateTime order by dateTime"""),
        NamedNativeQuery(
                name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_DAILY",
                query = """select date(s.timestamp) as dateTime, COUNT(*) as pageviews, COUNT(IF(DATE_ADD(p.publishedAt, interval 10 day) > s.timestamp, true, null)) as recentPageviews from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until and s.class <> 'Click' group by dateTime order by dateTime"""),
        NamedNativeQuery(
                name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_MONTHLY",
                query = """select date_sub(date(s.timestamp), interval day(s.timestamp) - 1 day) as dateTime, COUNT(*) as pageviews, COUNT(IF(DATE_ADD(p.publishedAt, interval 10 day) > s.timestamp, true, null)) as recentPageviews from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until and s.class <> 'Click' group by dateTime order by dateTime"""),
        NamedNativeQuery(
                name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_YEARLY",
                query = """select makedate(year(s.timestamp), 1) as dateTime, COUNT(*) as pageviews, COUNT(IF(DATE_ADD(p.publishedAt, interval 10 day) > s.timestamp, true, null)) as recentPageviews from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until and s.class <> 'Click' group by dateTime order by dateTime"""),
        NamedNativeQuery(
                name = "@GET_POST_STATS_ORDER_BY_PAGEVIEWS",
                query = """select p.id, p.title, p.publishedAt, group_concat(distinct pf.displayName separator ', ') as profileNames, c.name category, count(*) as pageviews, count(distinct v.id) as uniquePageviews from Stats s inner join Visitors v on s.visitorId = v.id inner join Posts p on s.postId = p.id inner join Categories c on p.categoryId = c.id left join Posts_Profiles pp on pp.postId = p.id left join Profiles pf on pf.id = pp.profileId where s.timestamp between :since and :until and p.status ='PUBLISH' group by p.id order by pageviews desc , p.id desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_PAGEVIEWS_ORDER_BY_AUTHOR",
                query = """select p.displayName as statName, count(*) as statValue from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until group by p.displayName order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_PAGEVIEWS_ORDER_BY_CATEGORY",
                query = """select c.name as statName, count(*) as statValue from Stats s inner join Posts p on s.postId = p.id inner join Categories c on p.categoryId = c.id where s.timestamp between :since and :until group by c.name order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_PAGES",
                query = """select ifnull(p.title, s.path) as statName, count(*) as statValue from Stats s left join Posts p on s.postId = p.id where s.timestamp between :since and :until and s.class <> 'Click' group by s.path order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_PAGES_BY_CLINT_TYPE",
                query = """select ifnull(p.title, s.path) as statName, count(*) as statValue from Stats s inner join Visitors v on s.visitorId = v.id left join Posts p on s.postId = p.id where s.timestamp between :since and :until and v.appName = :client group by s.path order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_POSTS",
                query = """select p.title as statName, count(*) as statValue from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until group by p.id order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_RECENT_POSTS",
                query = """select p.title as statName, count(*) as statValue from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until and date_add(p.publishedAt, interval 10 day) > s.timestamp group by p.id order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_OLD_POSTS",
                query = """select p.title as statName, count(*) as statValue from Stats s inner join Posts p on s.postId = p.id where s.timestamp between :since and :until and date_add(p.publishedAt, interval 10 day) <= s.timestamp group by p.id order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_POSTS_BY_CLINT_TYPE",
                query = """select p.title as statName, count(*) as statValue from Stats s inner join Posts p on s.postId = p.id inner join Visitors v on s.visitorId = v.id where s.timestamp between :since and :until and v.appName = :client group by p.id order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_LANDING_PAGE",
                query = """select ifnull(p.title, s.path) as statName, count(*) as statValue from Stats s left join Posts p on s.postId = p.id where s.timestamp between :since and :until and s.isLandingPage is true and s.class <> 'Click' group by s.path order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_LANDING_PAGE_BY_CLINT_TYPE",
                query = """select ifnull(p.title, s.path) as statName, count(*) as statValue from Stats s inner join Visitors v on s.visitorId = v.id left join Posts p on s.postId = p.id where s.timestamp between :since and :until and v.appName = :client and s.isLandingPage is true group by s.path order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_TAGS",
                query = """select t.name as statName, count(*) as statValue from Stats s inner join Posts_Tags pt on s.postId = pt.postId inner join Tags t on pt.tagId = t.id where s.timestamp between :since and :until group by t.id order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_TAGS_BY_CLIENT",
                query = """select t.name as statName, count(*) as statValue from Stats s inner join Visitors v on s.visitorId = v.id inner join Posts p on s.postId = p.id inner join Posts_Tags pt on p.id = pt.postId inner join Tags t on pt.tagId = t.id where s.timestamp between :since and :until and v.appName = :client group by t.id order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_VISITORS_TREND_HOURLY",
                query = """select date_add(date(v.timestamp), interval hour(v.timestamp) hour) as statDateTime, count(*) as statValue from Visitors as v where v.timestamp between :since and :until and v.isAdVisitor is false group by date(v.timestamp), hour(v.timestamp) order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_VISITORS_TREND_DAILY",
                query = """select date(v.timestamp) as statDateTime, count(*) as statValue from Visitors as v where v.timestamp between :since and :until and v.isAdVisitor is false group by date(v.timestamp) order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_VISITORS_TREND_MONTHLY",
                query = """select date_sub(date(v.timestamp), interval day(v.timestamp) - 1 day) as statDateTime, count(*) as statValue from Visitors as v where v.timestamp between :since and :until and v.isAdVisitor is false group by year(v.timestamp), month(v.timestamp) order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_VISITORS_TREND_YEARLY",
                query = """select makedate(year(v.timestamp), 1) as statDateTime, count(*) as statValue from Visitors as v where v.timestamp between :since and :until and v.isAdVisitor is false group by year(v.timestamp) order by statDateTime"""),
        NamedNativeQuery(
                name = "@GET_TOP_REFERRERS",
                query = """select v.referrer as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.isAdVisitor is false and v.referrer is not null group by v.referrer order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_REFERRERS_BY_CLIENT",
                query = """select v.referrer as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.referrer is not null and v.appName = :client group by v.referrer order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_WEB_BROWSERS",
                query = """select v.browser as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.isAdVisitor is false and v.referrer is not null group by v.browser order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_WEB_BROWSERS_BY_CLIENT",
                query = """select v.browser as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.referrer is not null and v.appName = :client group by v.browser order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_OS",
                query = """select v.os as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.isAdVisitor is false group by v.os order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_OS_BY_CLIENT",
                query = """select v.os as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.appName = :client group by v.os order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_CHANNELS",
                query = """select v.channel as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.isAdVisitor is false group by v.channel order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_TOP_CHANNELS_BY_CLIENT",
                query = """select v.channel as statName, count(*) as statValue from Visitors v where v.timestamp between :since and :until and v.appName = :client group by v.channel order by statValue desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_ALL_POST_STATS",
                query = """select p.id, p.title, p.publishedAt, group_concat(distinct pf.displayName separator ', ') as profileNames, c.name category, count(*) as pageviews, count(distinct v.id) as uniquePageviews from Stats s inner join Visitors v on s.visitorId = v.id inner join Posts p on s.postId = p.id inner join Categories c on p.categoryId = c.id left join Posts_Profiles pp on pp.postId = p.id left join Profiles pf on pf.id = pp.profileId where s.timestamp between :since and :until and p.status ='PUBLISH' group by p.id order by pageviews desc , p.id desc limit :limit"""),
        NamedNativeQuery(
                name = "@GET_ALL_POST_STATS_FROM_CACHE",
                query = """select p.id, p.title, p.publishedAt, s.profileNames profileNames, c.name category, s.pageviews, s.uniquePageviews from CachedPostStats s inner join Posts p on s.postId = p.id inner join Categories c on p.categoryId = c.id order by p.publishedAt desc"""),
        NamedNativeQuery(
                name = "@GET_LINKS_BY_CAMPAIGN_ID_ORDER_BY_CLICKS",
                query = """select l.id, l.campaignId, l.name, l.uid, l.url, l.createdAt, ifnull(i.clicks, 0) validClicks, l.linkType, b.id bannerId, b.bannerType, b.bgColor, b.imageUrl, b.internalUrl, b.isCover,  b.title, b.subtitle from Links l left join Banners b on b.linkId = l.id inner join Campaigns c on c.id = l.campaignId left outer join (   select l.id, count(distinct s.visitorId) clicks    from Campaigns c    inner join Links l on c.id = l.campaignId    inner join Stats s on l.id = s.linkId    where c.id = :id    and s.timestamp between c.startAt and c.endAt    group by l.id) i on i.id = l.id where c.id = :id order by clicks desc, id asc"""),
        NamedNativeQuery(
                name = "@GET_UV_TREND_HOURLY_BY_HOUR_BY_CAMPAIGN_ID",
                query = """select DATE_ADD(DATE(s.timestamp), interval HOUR(s.timestamp) hour) as statDateTime, COUNT(distinct s.visitorId) as statValue from Stats s inner join Links l on s.linkId = l.id inner join Campaigns c on c.id = l.campaignId where c.id = :id and s.timestamp between c.startAt and c.endAt group by statDateTime order by statDateTime"""))
data class NamedQueryHolder(
        @Id
        @GeneratedValue
        var id: Int = 0
)
