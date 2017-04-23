package com.indiepost.repository;

import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;
import com.indiepost.dto.StatResult;
import com.indiepost.enums.Types;
import com.indiepost.model.Stat;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 17.
 */
@Repository
public class StatRepositoryHibernate implements StatRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long save(Stat stat) {
        return (Long) getSession().save(stat);
    }

    @Override
    public void delete(Stat stat) {
        getSession().delete(stat);
    }

    @Override
    public Stat findById(Long id) {
        return entityManager.find(Stat.class, id);
    }

    @Override
    public void update(Stat stat) {
        getSession().update(stat);
    }

    @Override
    public Long getPageviewCount(Date since, Date until) {
        return null;
    }

    @Override
    public Long getVisitorCount(Date since, Date until) {
        return null;
    }

    @Override
    public List<StatResult> getPageviews(Date since, Date until, Types.Period period) {
        //TODO
        String sqlQuery = "SELECT ADDDATE(subdate(curdate(), INTERVAL 1 HOUR), INTERVAL hour(s.timestamp) HOUR) AS statDatetime, count(*) AS statCount " +
                "FROM Stats AS s GROUP BY HOUR(s.timestamp) ORDER BY statDatetime";
        return getSession().createSQLQuery(sqlQuery).setResultTransformer(new FluentHibernateResultTransformer(StatResult.class)).list();
    }

    @Override
    public List<StatResult> getVisitors(Date since, Date until, Types.Period period) {
        String sqlQuery = "SELECT date(s.timestamp) AS statDatetime, count(*) AS statCount FROM Visitors AS s GROUP BY HOUR(s.timestamp) ORDER BY statCount DESC";
        return getSession().createSQLQuery(sqlQuery).setResultTransformer(new FluentHibernateResultTransformer(StatResult.class)).list();
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }
}
