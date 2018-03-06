package com.indiepost.repository;

import com.indiepost.model.Word;
import org.springframework.data.repository.CrudRepository;

public interface DictionaryRepository extends CrudRepository<Word, Integer> {
    Word findBySurface(String surface);
}
