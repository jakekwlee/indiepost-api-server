package com.indiepost.config;

import com.indiepost.model.Tag;
import com.indiepost.service.TagService;
import org.apache.commons.lang3.text.StrBuilder;
import org.dozer.CustomConverter;
import org.dozer.DozerConverter;
import org.dozer.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jake on 16. 12. 12.
 */


@Component
public class TagSetToTagStringListConverter extends DozerConverter<Set, List> {

    @Autowired
    private TagService tagService;

    public TagSetToTagStringListConverter() {
        super(Set.class, List.class);
    }

    public List<String> convertTo(Set source, List destination) {
        if (source.size() > 0) {
            destination = new ArrayList<String>();
        }
        for (Object object : source) {
            Tag tag = (Tag) object;
            destination.add(tag.getName());
        }
        return destination;
    }

    public Set convertFrom(List source, Set destination) {
        if (source.size() > 0) {
            destination = new HashSet<Tag>();
        }
        List<String> tagStringArray = (List<String>) source;
        for (String str : tagStringArray) {
            Tag tag = tagService.findByName(str);
            if (tag == null) {
                tag = new Tag();
                tag.setName(str.trim());
                tagService.save(tag);
            }
            destination.add(tag);
        }
        return destination;
    }
}