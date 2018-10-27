package com.indiepost.service

import com.indiepost.model.Word

interface DictionaryService {

    val words: List<Word>

    fun remove(text: String)

    fun save(word: Word)
}
