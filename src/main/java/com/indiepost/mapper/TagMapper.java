package com.indiepost.mapper;

import com.indiepost.model.Tag;
import dto.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 16. 12. 14.
 */
@Mapper(componentModel = "spring")
public abstract class TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    abstract public TagDto tagToTagDto(Tag tag);

    @Mapping(target = "posts", ignore = true)
    abstract public Tag tagDtoToTag(TagDto tagDto);

    public Set<Tag> tagStringListToTagSet(List<String> tagStringList) {
        Set<Tag> tags = new HashSet<>();
        for (String tagString : tagStringList) {
            Tag tag = new Tag();
            tag.setName(tagString);
            tags.add(tag);
        }
        return tags;
    }

    public List<String> tagSetToTagStringList(Set<Tag> tags) {
        List<String> tagStringList = new ArrayList<>();
        for (Tag tag : tags) {
            tagStringList.add(tag.getName());
        }
        return tagStringList;
    }
}
