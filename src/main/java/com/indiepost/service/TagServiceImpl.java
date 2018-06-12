package com.indiepost.service;

import com.indiepost.model.Tag;
import com.indiepost.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Inject
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Tag findById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findByTagName(name);
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public List<String> findAllToStringList() {
        List<Tag> tags = findAll();
        List<String> result = new ArrayList<>();
        for (Tag tag : tags) {
            result.add(tag.getName());
        }
        return result;
    }

    @Override
    public List<Tag> findAll(int page, int maxResults) {
        return tagRepository.findAll(PageRequest.of(page, maxResults, Sort.Direction.DESC, "id"));
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }


}
