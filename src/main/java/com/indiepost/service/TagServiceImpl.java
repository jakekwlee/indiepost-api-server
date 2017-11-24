package com.indiepost.service;

import com.google.common.collect.Lists;
import com.indiepost.model.Tag;
import com.indiepost.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 9/17/16.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Tag findById(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    public Tag findByName(String name) {
        return tagRepository.findOneByName(name);
    }

    @Override
    public List<Tag> findAll() {
        return Lists.newArrayList(tagRepository.findAll());
    }

    @Override
    public List<String> findAllToStringList() {
        List<Tag> tags = findAll();
        return tags.stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toList());
    }

    @Override
    public List<Tag> findAll(int page, int maxResults) {
        Pageable pageable = new PageRequest(page, maxResults, Sort.Direction.DESC, "id");
        return Lists.newArrayList(tagRepository.findAll(pageable));
    }

    @Override
    public void update(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }


}
