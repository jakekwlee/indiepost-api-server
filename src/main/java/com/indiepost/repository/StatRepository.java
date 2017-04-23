package com.indiepost.repository;

import com.indiepost.dto.StatResult;
import com.indiepost.enums.Types.Period;
import com.indiepost.model.Stat;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 4. 17.
 */
public interface StatRepository {

    Long save(Stat stat);

    void delete(Stat stat);

    Stat findById(Long id);

    void update(Stat stat);

    Long getPageviewCount(Date since, Date until);

    Long getVisitorCount(Date since, Date until);

    List<StatResult> getPageviews(Date since, Date until, Period period);

    List<StatResult> getVisitors(Date since, Date until, Period period);
}
