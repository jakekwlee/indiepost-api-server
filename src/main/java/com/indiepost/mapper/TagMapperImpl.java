package com.indiepost.mapper;

import com.indiepost.model.Tag;
import com.indiepost.service.TagService;
import dto.TagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 16. 12. 15.
 */
@Component
public class TagMapperImpl implements TagMapper {

    @Autowired
    private TagService tagService;

    @Override
    public TagDto tagToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(tag.getId());
        tagDto.setName(tag.getName());
        return tagDto;
    }

    @Override
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
    public Set<Tag> tagStringListToTagSet(List<String> tagStringList) {
        Set<Tag> tags = new HashSet<>();
        for (String tagString : tagStringList) {
            Tag tag = new Tag();
            tag.setName(tagString);
            tags.add(tag);
        }
        return tags;
    }

    @Override
    public List<String> tagSetToTagStringList(Set<Tag> tags) {
        List<String> tagStringList = new ArrayList<>();
        for (Tag tag : tags) {
            tagStringList.add(tag.getName());
        }
        return tagStringList;
    }
}
