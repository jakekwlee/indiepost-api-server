package com.indiepost.service.mapper;

import com.indiepost.dto.TagDto;
import com.indiepost.model.Tag;

import java.util.List;

/**
 * Created by jake on 16. 12. 14.
 */
public interface TagMapperService {

    TagDto tagToTagDto(Tag tag);

    Tag tagDtoToTag(TagDto tagDto);

    List<Tag> tagStringListToTagList(List<String> tagStringList);

    List<String> tagListToTagStringList(List<Tag> tagList);

    List<TagDto> tagListToTagDtoList(List<Tag> tagList);

    List<Tag> tagDtoListToTagList(List<TagDto> tagDtoList);

}
