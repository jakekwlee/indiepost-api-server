package com.indiepost.model.analytics;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Created by jake on 17. 4. 13.
 */
@Entity
@Table(name = "Stats", indexes = {
        @Index(columnList = "timestamp", name = "s_timestamp_idx")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "class", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Stat")
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
                query = "SELECT count(DISTINCT s.path, v.id) " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id AND v.adVisitor IS FALSE " +
                        "WHERE s.timestamp BETWEEN :since AND :until"),

        @NamedNativeQuery(name = "@GET_TOTAL_UNIQUE_PAGEVIEWS_ON_POSTS_BY_CLIENT",
                query = "SELECT count(DISTINCT s.path, v.id) " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND v.appName = :client"),

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

        @NamedNativeQuery(name = "@GET_POSTS_ORDER_BY_PAGEVIEWS",
                query = "SELECT p.id AS id, p.title AS title, p.displayName AS author, c.name AS category, count(*) AS pageview " +
                        "FROM Stats s " +
                        "INNER JOIN Posts p ON s.postId = p.id INNER JOIN Categories c ON p.categoryId = c.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until " +
                        "GROUP BY p.id ORDER BY pageview DESC , p.id DESC LIMIT :limit"),

        @NamedNativeQuery(name = "@GET_POSTS_ORDER_BY_UNIQUE_PAGEVIEWS",
                query = "SELECT s.postId AS id, count(DISTINCT v.id) AS uniquePageview " +
                        "FROM Stats s INNER JOIN Visitors v ON s.visitorId = v.id " +
                        "WHERE s.timestamp BETWEEN :since AND :until AND s.postId IS NOT NULL " +
                        "GROUP BY s.postId ORDER BY uniquePageview DESC, s.postId DESC LIMIT :limit"),

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
                        "LIMIT :limit")
})
public class Stat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 200)
    private String path;

    @NotNull
    private LocalDateTime timestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "visitorId", updatable = false, insertable = false, nullable = false)
    private Visitor visitor;

    @NotNull
    @Column(name = "visitorId", nullable = false)
    private Long visitorId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Long getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(Long visitorId) {
        this.visitorId = visitorId;
    }

}
