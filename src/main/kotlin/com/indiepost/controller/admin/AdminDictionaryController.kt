package com.indiepost.controller.admin

import com.indiepost.model.Word
import com.indiepost.service.DictionaryService
import org.springframework.web.bind.annotation.*

import javax.inject.Inject

@RestController
@RequestMapping(value = ["/admin/dictionary"], produces = ["application/json; charset=UTF-8"])
class AdminDictionaryController @Inject constructor(private val dictionaryService: DictionaryService) {

    @PutMapping
    fun save(@RequestBody word: Word) {
        dictionaryService.save(word)
    }

    @DeleteMapping
    fun remove(@RequestBody word: Word) {
        dictionaryService.remove(word.surface!!)
    }

    @GetMapping
    fun getWords(): List<Word> {
        return dictionaryService.words
    }

}
