package com.indiepost.service;

import com.indiepost.dto.TagDto;
import com.indiepost.model.Tag;
import com.indiepost.repository.TagRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

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
    public TagDto save(Tag tag) {
        tagRepository.save(tag);
        return new TagDto(tag.getId(), tag.getName());
    }

    @Override
    public TagDto findById(Long id) {
        Tag tag = tagRepository.findOne(id);
        return new TagDto(tag.getId(), tag.getName());
    }

    @Override
    public TagDto findByName(String name) {
        Tag tag = tagRepository.findOneByName(name);
        return new TagDto(tag.getId(), tag.getName());
    }

    @Override
    public List<String> findAllToStringList() {
        Pageable pageable = new PageRequest(0, 9999999, Sort.Direction.DESC, "id");
        List<Tag> tags = tagRepository.findAll(pageable);
        return tags.stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDto> findAll(int page, int maxResults) {
        Pageable pageable = new PageRequest(page, maxResults, Sort.Direction.DESC, "id");
        List<Tag> tags = tagRepository.findAll(pageable);
        return tags.stream()
                .map(tag -> new TagDto(tag.getId(),
                        tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TagDto> findByIds(List<Long> ids) {
        List<Tag> tags = tagRepository.findByIdIn(ids);
        return tags.stream()
                .map(tag -> new TagDto(tag.getId(), tag.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public void update(TagDto tagDto) {
        Tag tag = tagRepository.findOne(tagDto.getId());
        tag.setName(tagDto.getName());
        tagRepository.save(tag);
    }


    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }
}
