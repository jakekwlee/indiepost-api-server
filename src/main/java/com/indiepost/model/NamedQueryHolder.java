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

        @NamedNativeQuery(name = "@GET_POST_STATS_ORDER_BY_PAGEVIEWS",
                query = "SELECT p.id, p.title, p.displayName AS author, c.name AS category, " +
                        "p.publishedAt, count(*) AS pageview, count(DISTINCT v.id) AS uniquePageview " +
                        "FROM Stats s " +
                        "INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "INNER JOIN Posts p ON s.postId = p.id INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "AND p.status ='PUBLISH' " +
                        "GROUP BY p.id ORDER BY pageview DESC , p.id DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_PAGEVIEWS_ORDER_BY_AUTHOR",
                query = "SELECT p.displayName AS statName, count(*) AS statValue " +
                        "FROM Stats s INNER JOIN Posts p ON s.postId = p.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "GROUP BY p.displayName ORDER BY statValue DESC LIMIT :limit"),

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
                        "INNER JOIN Posts p ON s.postId = p.id " +
                        "INNER JOIN Posts_Tags pt ON p.id = pt.postId " +
                        "INNER JOIN Tags t ON pt.tagId = t.id " +
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
        @NamedNativeQuery(name = "@UPDATE_POST_PAGEVIEWS",
                query = "INSERT INTO PostStats (postId, pageview, uniquePageview)" +
                        "   SELECT i.postId, i.ipv, i.iuv FROM (" +
                        "       SELECT p.id AS postId, COUNT(*) AS ipv, COUNT(DISTINCT v.id) AS iuv" +
                        "       FROM Posts p" +
                        "       INNER JOIN Stats s ON s.postId = p.id" +
                        "       INNER JOIN Visitors v ON v.id = s.visitorId" +
                        "       WHERE s.timestamp > (SELECT postPageviewLastUpdatedAt FROM StatMetadata)" +
                        "       AND s.timestamp <= :now" +
                        "       AND p.status = 'PUBLISH' " +
                        "       GROUP BY p.id" +
                        "       ORDER BY p.publishedAt DESC)" +
                        "      AS i " +
                        "ON DUPLICATE KEY UPDATE pageview = pageview + i.ipv, uniquePageview = uniquePageview + i.iuv"),

        @NamedNativeQuery(name = "@GET_ALL_POST_STATS",
                query = "SELECT p.id, p.title, p.publishedAt, p.displayName author, c.name category, " +
                        "s.pageview, s.uniquePageview FROM PostStats s " +
                        "INNER JOIN Posts p ON p.id = s.postId " +
                        "INNER JOIN Categories c ON c.id = p.categoryId " +
                        "WHERE p.status = 'PUBLISH' " +
                        "ORDER BY p.publishedAt DESC")
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
