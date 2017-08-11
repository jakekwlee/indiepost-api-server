package com.indiepost.service;

import com.indiepost.dto.LinkDto;
import com.indiepost.model.analytics.Link;

import java.util.List;

/**
 * Created by jake on 8/10/17.
 */
public interface LinkService {

    LinkDto save(LinkDto linkDto);

    void update(LinkDto linkDto);

    void deleteById(Long id);

    LinkDto findById(Long id);

    LinkDto findByUid(String uid);

    List<LinkDto> findAll();

    Link dtoToLink(LinkDto linkDto);

    LinkDto linkToDto(Link link);

}
