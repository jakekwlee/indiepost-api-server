package com.indiepost.repository;

import com.indiepost.model.analytics.Click;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by jake on 8/9/17.
 */
public interface ClickRepository extends CrudRepository<Click, Long> {
}
