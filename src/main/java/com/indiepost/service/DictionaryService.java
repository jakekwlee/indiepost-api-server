package com.indiepost.service;

import com.indiepost.model.Word;

import java.util.List;

public interface DictionaryService {

    void remove(String text);

    void save(Word word);

    List<Word> getWords();
}
