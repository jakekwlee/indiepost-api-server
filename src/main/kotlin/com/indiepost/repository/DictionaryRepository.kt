package com.indiepost.repository

import com.indiepost.model.Word
import org.springframework.data.repository.CrudRepository

interface DictionaryRepository : CrudRepository<Word, Int> {
    fun findBySurface(surface: String): Word?
}
