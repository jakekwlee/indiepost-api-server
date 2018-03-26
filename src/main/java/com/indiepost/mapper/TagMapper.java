package com.indiepost.mapper;

import com.indiepost.dto.TagDto;
import com.indiepost.model.Tag;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jake on 16. 12. 15.
 */
public class TagMapper {

    public static TagDto tagToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        BeanUtils.copyProperties(tag, tagDto);
        return tagDto;
    }

    public static Tag tagDtoToTag(TagDto tagDto) {
        Tag tag = new Tag();
        if (tagDto.getId() != null) {
            tag.setId(tagDto.getId());
        }
        tag.setName(tagDto.getName());
        return tag;
    }

    public static List<Tag> tagStringListToTagList(List<String> tagStringList) {
        List<Tag> tags = new ArrayList<>();
        for (String tagString : tagStringList) {
            Tag tag = new Tag();
            tag.setName(tagString);
            tags.add(tag);
        }
        return tags;
    }

    public static List<String> tagsToTagStrings(List<Tag> tagList) {
        List<String> tagStringList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagStringList.add(tag.getName());
        }
        return tagStringList;
    }

    public static List<TagDto> tagListToTagDtoList(List<Tag> tagList) {
        List<TagDto> tagDtoList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagDtoList.add(tagToTagDto(tag));
        }
        return tagDtoList;
    }

    public static List<Tag> tagDtoListToTagList(List<TagDto> tagDtoList) {
        return tagDtoList.stream()
                .map(tagDto -> tagDtoToTag(tagDto))
                .collect(Collectors.toList());
    }
}
