package com.indiepost.repository;

import com.indiepost.model.Visitor;

/**
 * Created by jake on 17. 4. 9.
 */
public interface VisitorRepository {
    Long save(Visitor visitor);

    void update(Visitor visitor);

    Visitor findById(Long id);
}
