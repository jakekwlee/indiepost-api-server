package com.indiepost.service;

import com.google.common.collect.Lists;
import com.indiepost.model.Word;
import com.indiepost.repository.DictionaryRepository;
import com.indiepost.repository.elasticsearch.PostEsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    private final DictionaryRepository dictionaryRepository;

    private final PostEsRepository postEsRepository;

    @Autowired
    public DictionaryServiceImpl(DictionaryRepository dictionaryRepository, PostEsRepository postEsRepository) {
        this.dictionaryRepository = dictionaryRepository;
        this.postEsRepository = postEsRepository;
    }

    @Override
    public void remove(String text) {
        Word word = dictionaryRepository.findBySurface(text);
        dictionaryRepository.delete(word);
        updateDictionary();
    }

    @Override
    public void save(Word word) {
        if (word.getId() == null) {
            Word saved = dictionaryRepository.findBySurface(word.getSurface());
            if (saved == null) {
                dictionaryRepository.save(word);
            } else {
                saved.setCost(word.getCost());
                dictionaryRepository.save(saved);
            }
        } else {
            dictionaryRepository.save(word);
        }
        updateDictionary();
    }

    @Override
    public List<Word> getWords() {
        return Lists.newArrayList(dictionaryRepository.findAll());
    }

    private void updateDictionary() {
        Iterable iterable = dictionaryRepository.findAll();
        List<Word> words = Lists.newArrayList(iterable);
        postEsRepository.updateDictionary(words);
    }
}
