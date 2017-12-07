package com.indiepost.repository;

import com.indiepost.model.analytics.Action;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jake on 8/9/17.
 */
public interface ActionRepository extends CrudRepository<Action, Long> {

}
