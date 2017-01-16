package com.indiepost.mapper;

import com.indiepost.model.Tag;
import com.indiepost.dto.TagDto;

import java.util.List;

/**
 * Created by jake on 16. 12. 14.
 */
public interface TagMapper {

    TagDto tagToTagDto(Tag tag);

    Tag tagDtoToTag(TagDto tagDto);

    List<Tag> tagStringListToTagList(List<String> tagStringList);

    List<String> tagListToTagStringList(List<Tag> tagList);

    List<TagDto> tagListToTagDtoList(List<Tag> tagList);

    List<Tag> tagDtoListToTagList(List<TagDto> tagDtoList);

}
