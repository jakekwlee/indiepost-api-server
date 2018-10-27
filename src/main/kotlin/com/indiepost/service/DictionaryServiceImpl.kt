package com.indiepost.service

import com.google.common.collect.Lists
import com.indiepost.model.Word
import com.indiepost.repository.DictionaryRepository
import com.indiepost.repository.elasticsearch.PostEsRepository
import org.springframework.stereotype.Service

import javax.inject.Inject

@Service
class DictionaryServiceImpl @Inject
constructor(private val dictionaryRepository: DictionaryRepository, private val postEsRepository: PostEsRepository) : DictionaryService {

    override val words: List<Word>
        get() = Lists.newArrayList(dictionaryRepository.findAll())

    override fun remove(text: String) {
        val word = dictionaryRepository.findBySurface(text) ?: return
        dictionaryRepository.delete(word)
        updateDictionary()
    }

    override fun save(word: Word) {
        if (word.id == null) {
            val saved = dictionaryRepository.findBySurface(word.surface!!)
            if (saved == null) {
                dictionaryRepository.save(word)
            } else {
                saved.cost = word.cost
                dictionaryRepository.save(saved)
            }
        } else {
            dictionaryRepository.save(word)
        }
        updateDictionary()
    }

    private fun updateDictionary() {
        val words = dictionaryRepository.findAll().toList()
        postEsRepository.updateDictionary(words)
    }
}
