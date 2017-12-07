package com.indiepost.model;

import javax.persistence.*;

/**
 * Created by jake on 8/15/17.
 */
@Entity
@NamedNativeQueries({
        @NamedNativeQuery(name = "@GET_TOTAL_UNIQUE_PAGEVIEWS",
                query = "SELECT count(DISTINCT s.path, v.id) " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id AND v.adVisitor IS FALSE " +
                        "WHERE s.timestamp BETWEEN :since AND :until"),

        @NamedNativeQuery(name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_BY_CLIENT",
                query = "SELECT count(DISTINCT s.path, v.id) " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND v.appName = :client"),

        @NamedNativeQuery(name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS",
                query = "SELECT count(DISTINCT s.postId, v.id) " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id AND v.adVisitor IS FALSE " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.postId IS NOT NULL"),

        @NamedNativeQuery(name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS_BY_CLIENT",
                query = "SELECT count(DISTINCT s.postId, v.id) " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND v.appName = :client AND s.postId IS NOT NULL"),


        @NamedNativeQuery(name = "@GET_PAGEVIEW_TREND_HOURLY",
                query = "SELECT date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS statDateTime, count(*) AS statValue " +
                        "FROM Stats s " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY date(s.timestamp), hour(s.timestamp) ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_PAGEVIEW_TREND_DAILY",
                query = "SELECT date(s.timestamp) AS statDateTime, count(*) AS statValue " +
                        "FROM Stats s " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY date(s.timestamp) ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_PAGEVIEW_TREND_MONTHLY",
                query = "SELECT date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS statDateTime, count(*) AS statValue  " +
                        "FROM Stats s " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY year(s.timestamp), month(s.timestamp) "),

        @NamedNativeQuery(name = "@GET_PAGEVIEW_TREND_YEARLY",
                query = "SELECT makedate(year(s.timestamp), 1) AS statDateTime, count(*) AS statValue " +
                        "FROM Stats s " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY year(s.timestamp) ORDER BY statDateTime"),


        @NamedNativeQuery(name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_HOURLY",
                query = "SELECT date_add(date(s.timestamp), INTERVAL hour(s.timestamp) HOUR) AS dateTime, " +
                        "COUNT(*) AS pageviews, " +
                        "COUNT(IF(DATE_ADD(p.publishedAt, INTERVAL 10 DAY) > s.timestamp, TRUE, NULL)) AS recentPageviews " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY date(s.timestamp), hour(s.timestamp) ORDER BY dateTime"),

        @NamedNativeQuery(name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_DAILY",
                query = "SELECT date(s.timestamp) AS dateTime, " +
                        "COUNT(*) AS pageviews, " +
                        "COUNT(IF(DATE_ADD(p.publishedAt, INTERVAL 10 DAY) > s.timestamp, TRUE, NULL)) AS recentPageviews " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY date(s.timestamp) ORDER BY dateTime"),

        @NamedNativeQuery(name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_MONTHLY",
                query = "SELECT date_sub(date(s.timestamp), INTERVAL day(s.timestamp) - 1 DAY) AS dateTime, " +
                        "COUNT(*) AS pageviews, " +
                        "COUNT(IF(DATE_ADD(p.publishedAt, INTERVAL 10 DAY) > s.timestamp, TRUE, NULL)) AS recentPageviews " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY year(s.timestamp), month(s.timestamp) ORDER BY dateTime"),

        @NamedNativeQuery(name = "@GET_OLD_AND_NEW_PAGEVIEW_TREND_YEARLY",
                query = "SELECT makedate(year(s.timestamp), 1) AS dateTime, " +
                        "COUNT(*) AS pageviews, " +
                        "COUNT(IF(DATE_ADD(p.publishedAt, INTERVAL 10 DAY) > s.timestamp, TRUE, NULL)) AS recentPageviews " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY year(s.timestamp) ORDER BY dateTime"),


        @NamedNativeQuery(name = "@GET_POST_STATS_ORDER_BY_PAGEVIEWS",
                query = "SELECT p.id, p.title, p.bylineName AS author, c.name AS category, " +
                        "p.publishedAt, count(*) AS pageviews, count(DISTINCT v.id) AS uniquePageviews " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "AND p.status ='PUBLISH' " +
                        "GROUP BY p.id ORDER BY pageviews DESC , p.id DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_PAGEVIEWS_ORDER_BY_AUTHOR",
                query = "SELECT p.bylineName AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "GROUP BY p.bylineName ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_PAGEVIEWS_ORDER_BY_CATEGORY",
                query = "SELECT c.name AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "GROUP BY c.name ORDER BY statValue DESC LIMIT :limit"),


        @NamedNativeQuery(name = "@GET_TOP_PAGES",
                query = "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statValue " +
                        "FROM Stats s LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.class <> 'Click' " +
                        "GROUP BY s.path ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_PAGES_BY_CLINT_TYPE",
                query = "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND v.appName = :client " +
                        "GROUP BY s.path ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_POSTS",
                query = "SELECT p.title AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "GROUP BY p.id ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_RECENT_POSTS",
                query = "SELECT p.title AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "AND date_add(p.publishedAt, INTERVAL 10 DAY) > s.timestamp " +
                        "GROUP BY p.id ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_OLD_POSTS",
                query = "SELECT p.title AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "AND date_add(p.publishedAt, INTERVAL 10 DAY) <= s.timestamp " +
                        "GROUP BY p.id ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_POSTS_BY_CLINT_TYPE",
                query = "SELECT p.title AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND v.appName = :client " +
                        "GROUP BY p.id ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_LANDING_PAGE",
                query = "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statValue " +
                        "FROM Stats s LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.isLandingPage IS TRUE AND s.class <> 'Click' " +
                        "GROUP BY s.path ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_LANDING_PAGE_BY_CLINT_TYPE",
                query = "SELECT ifnull(p.title, s.path) AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id LEFT JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND v.appName = :client AND s.isLandingPage IS TRUE " +
                        "GROUP BY s.path ORDER BY statValue DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_TAGS",
                query = "SELECT t.name AS statName, count(*) AS statValue FROM Stats s " +
                        "INNER JOIN Posts_Tags pt ON s.postId = pt.post_id " +
                        "INNER JOIN Tags t ON pt.tag_id = t.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "GROUP BY t.id " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_TAGS_BY_CLIENT",
                query = "SELECT t.name AS statName, count(*) AS statValue FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Posts_Tags pt ON p.id = pt.postId " +
                        "INNER JOIN Tags t ON pt.tagId = t.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "AND v.appName = :client " +
                        "GROUP BY t.id " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),


        @NamedNativeQuery(name = "@GET_VISITORS_TREND_HOURLY",
                query = "SELECT date_add(date(v.timestamp), INTERVAL hour(v.timestamp) HOUR) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY date(v.timestamp), hour(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_VISITORS_TREND_DAILY",
                query = "SELECT date(v.timestamp) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY date(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_VISITORS_TREND_MONTHLY",
                query = "SELECT date_sub(date(v.timestamp), INTERVAL day(v.timestamp) - 1 DAY) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY year(v.timestamp), month(v.timestamp) " +
                        "ORDER BY statDateTime"),

        @NamedNativeQuery(name = "@GET_VISITORS_TREND_YEARLY",
                query = "SELECT makedate(year(v.timestamp), 1) AS statDateTime, count(*) AS statValue " +
                        "FROM Visitors AS v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY year(v.timestamp) " +
                        "ORDER BY statDateTime"),


        @NamedNativeQuery(name = "@GET_TOP_REFERRERS",
                query = "SELECT v.referrer AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "AND v.referrer IS NOT NULL " +
                        "GROUP BY v.referrer " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_REFERRERS_BY_CLIENT",
                query = "SELECT v.referrer AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.referrer IS NOT NULL " +
                        "AND v.appName = :client " +
                        "GROUP BY v.referrer " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_WEB_BROWSERS",
                query = "SELECT v.browser AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "AND v.referrer IS NOT NULL " +
                        "GROUP BY v.browser " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_WEB_BROWSERS_BY_CLIENT",
                query = "SELECT v.browser AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.referrer IS NOT NULL " +
                        "AND v.appName = :client " +
                        "GROUP BY v.browser " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_OS",
                query = "SELECT v.os AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY v.os " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_OS_BY_CLIENT",
                query = "SELECT v.os AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.appName = :client " +
                        "GROUP BY v.os " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_CHANNELS",
                query = "SELECT v.channel AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until AND v.adVisitor IS FALSE " +
                        "GROUP BY v.channel " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_TOP_CHANNELS_BY_CLIENT",
                query = "SELECT v.channel AS statName, count(*) AS statValue " +
                        "FROM Visitors v " +
                        "WHERE v.timestamp BETWEEN :since AND :until " +
                        "AND v.appName = :client " +
                        "GROUP BY v.channel " +
                        "ORDER BY statValue DESC " +
                        "LIMIT :limit"),


        @NamedNativeQuery(name = "@GET_ALL_POST_STATS",
                query = "SELECT p.id, p.title, p.publishedAt, p.bylineName author, c.name category, " +
                        "count(*) AS pageviews, count(DISTINCT s.visitorId) AS uniquePageviews " +
                        "FROM Posts p " +
                        "LEFT JOIN Stats s ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "AND p.status = 'PUBLISH' " +
                        "GROUP BY p.id " +
                        "ORDER BY p.publishedAt DESC"),

        @NamedNativeQuery(name = "@GET_ALL_POST_STATS_FROM_CACHE",
                query = "SELECT p.id, p.title, p.publishedAt, p.bylineName author, c.name category, " +
                        "s.pageviews, s.uniquePageviews " +
                        "FROM CachedPostStats s " +
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "ORDER BY p.publishedAt DESC"),

        @NamedNativeQuery(name = "@UPDATE_ALL_POST_STATS_CACHE",
                query = "INSERT INTO CachedPostStats  " +
                        "(postId, pageviews, uniquePageviews, legacyPageviews, legacyUniquePageviews) " +
                        "SELECT p.id postId, count(*) AS pageviews, count(DISTINCT s.visitorId) AS uniquePageviews, " +
                        "ifnull(l.pageviews, 0) AS legacyPageviews, ifnull(l.uniquePageviews, 0) AS legacyUniquePageviews " +
                        "FROM Posts p " +
                        "LEFT OUTER JOIN LegacyStats l ON p.id = l.postId " +
                        "LEFT OUTER JOIN Stats s ON s.postId = p.id " +
                        "INNER JOIN Categories c ON p.categoryId = c.id " +
                        "AND p.status = 'PUBLISH' " +
                        "GROUP BY p.id " +
                        "ORDER BY p.publishedAt DESC"
        )
})
public class NamedQueryHolder {

    @Id
    @GeneratedValue
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
