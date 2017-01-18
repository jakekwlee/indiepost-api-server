package com.indiepost.service.mapper;

import com.indiepost.dto.TagDto;
import com.indiepost.model.Tag;
import com.indiepost.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 16. 12. 15.
 */
@Service
public class TagMapperServiceImpl implements TagMapperService {

    private final TagService tagService;

    @Autowired
    public TagMapperServiceImpl(TagService tagService) {
        this.tagService = tagService;
    }

    @Override
    public TagDto tagToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    @Override
    @Transactional
    public Tag tagDtoToTag(TagDto tagDto) {
        Tag tag = tagService.findByName(tagDto.getName());
        if (tag == null) {
            tag = new Tag();
            tag.setName(tagDto.getName());
            tagService.save(tag);
        }
        return tag;
    }

    @Override
    public List<Tag> tagStringListToTagList(List<String> tagStringList) {
        List<Tag> tags = new ArrayList<>();
        for (String tagString : tagStringList) {
            Tag tag = new Tag();
            tag.setName(tagString);
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public List<String> tagListToTagStringList(List<Tag> tagList) {
        List<String> tagStringList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagStringList.add(tag.getName());
        }
        return tagStringList;
    }

    @Override
    public List<TagDto> tagListToTagDtoList(List<Tag> tagList) {
        List<TagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagDtoList.add(this.tagToTagDto(tag));
        }
        return tagDtoList;
    }

    @Override
    public List<Tag> tagDtoListToTagList(List<TagDto> tagDtoList) {
        List<Tag> tagList = new ArrayList<>();
        for (TagDto tagDto : tagDtoList) {
            tagList.add(this.tagDtoToTag(tagDto));
        }
        return tagList;
    }
}
