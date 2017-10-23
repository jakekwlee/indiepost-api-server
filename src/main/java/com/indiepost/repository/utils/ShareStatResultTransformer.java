package com.indiepost.repository.utils;

import com.indiepost.dto.stat.ShareStat;
import org.hibernate.transform.ResultTransformer;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by jake on 10/24/17.
 */
public class ShareStatResultTransformer implements ResultTransformer {
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        return new ShareStat(
                (String) tuple[0],
                ((BigInteger) tuple[1]).longValue()
        );
    }

    @Override
    public List transformList(List collection) {
        return collection;
    }
}
