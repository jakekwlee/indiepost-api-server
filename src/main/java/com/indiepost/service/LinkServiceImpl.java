package com.indiepost.service;

import com.indiepost.dto.stat.LinkDto;
import com.indiepost.model.analytics.Link;
import com.indiepost.repository.ClickRepository;
import com.indiepost.repository.LinkRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by jake on 8/10/17.
 */
@Service
@Transactional
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;

    private final ClickRepository clickRepository;

    @Autowired
    public LinkServiceImpl(LinkRepository linkRepository, ClickRepository clickRepository) {
        this.linkRepository = linkRepository;
        this.clickRepository = clickRepository;
    }

    @Override
    public LinkDto save(LinkDto linkDto) {
        Link link = dtoToLink(linkDto);
        linkRepository.save(link);
        return linkToDto(link);
    }

    @Override
    public void update(LinkDto linkDto) {
        Link link = dtoToLink(linkDto);
        linkRepository.save(link);
    }

    @Override
    public void deleteById(Long id) {
        linkRepository.deleteById(id);
    }

    @Override
    public LinkDto findById(Long id) {
        Optional<Link> optional = linkRepository.findById(id);
        if (optional.isPresent()) {
            Link link = optional.get();
            return linkToDto(link);
        } else {
            return null;
        }
    }

    @Override
    public LinkDto findByUid(String uid) {
        if (!isValidUid(uid)) {
            return null;
        }
        Link link = linkRepository.findByUid(uid);
        return linkToDto(link);
    }

    @Override
    public List<LinkDto> findAll() {
        List<Link> links = (List<Link>) linkRepository.findAll();
        return links.stream()
                .map(link -> linkToDto(link))
                .collect(Collectors.toList());
    }

    @Override
    public Link dtoToLink(LinkDto linkDto) {
        Link link = new Link();
        link.setCampaignId(linkDto.getCampaignId());
        link.setName(linkDto.getName());
        link.setUrl(linkDto.getUrl());
        link.setCreatedAt(LocalDateTime.now());
        link.setUid(RandomStringUtils.randomAlphanumeric(8));
        return link;
    }

    @Override
    public LinkDto linkToDto(Link link) {
        LinkDto linkDto = new LinkDto();
        linkDto.setId(link.getId());
        linkDto.setCampaignId(link.getCampaignId());
        linkDto.setCreatedAt(link.getCreatedAt());
        linkDto.setName(link.getName());
        linkDto.setUid(link.getUid());

        Long allClickCount = clickRepository.countByLinkId(link.getId());
        Long validClickCount = clickRepository.countValidClicksByLinkId(link.getId());
        linkDto.setAllClicks(allClickCount);
        linkDto.setAllClicks(validClickCount);
        return linkDto;
    }

    private boolean isValidUid(String s) {
        String pattern = "^[a-zA-Z0-9]{8}$";
        return s.matches(pattern);
    }

}
