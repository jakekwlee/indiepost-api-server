package com.indiepost.mapper;

import com.indiepost.model.Tag;
import dto.TagDto;

import java.util.List;
import java.util.Set;

/**
 * Created by jake on 16. 12. 14.
 */
public interface TagMapper {

    TagDto tagToTagDto(Tag tag);

    Tag tagDtoToTag(TagDto tagDto);

    Set<Tag> tagStringListToTagSet(List<String> tagStringList);

    List<String> tagSetToTagStringList(Set<Tag> tags);

}
