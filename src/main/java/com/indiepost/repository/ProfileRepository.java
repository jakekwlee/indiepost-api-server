package com.indiepost.repository;

import com.indiepost.model.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    List<Profile> findByIdIn(List<Long> ids);

    List<Profile> findAllByOrderByCreatedAtAsc();

    List<Profile> findAllByOrderByCreatedAtDesc();
}
