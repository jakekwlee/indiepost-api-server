package com.indiepost.service;

import com.indiepost.model.Tag;
import com.indiepost.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jake on 9/17/16.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

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
    public String[] findAllToStringArray() {
        List<Tag> tags = findAll();
        String[] tagArray = new String[tags.size()];
        for (int i = 0; i < tagArray.length; ++i) {
            tagArray[i] = tags.get(i).getName();
        }
        return tagArray;
    }

    @Override
    public List<Tag> findAll(int page, int maxResults) {
        return tagRepository.findAll(new PageRequest(page, maxResults, Sort.Direction.DESC, "id"));
    }

    @Override
    public void update(Tag tag) {
        tagRepository.update(tag);
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }


}
