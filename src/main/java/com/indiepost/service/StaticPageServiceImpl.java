package com.indiepost.service;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.StaticPage;
import com.indiepost.model.User;
import com.indiepost.repository.StaticPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Created by jake on 17. 3. 5.
 */
@Service
@Transactional
public class StaticPageServiceImpl implements StaticPageService {

    private final StaticPageRepository staticPageRepository;

    private final UserService userService;

    @Autowired
    public StaticPageServiceImpl(StaticPageRepository staticPageRepository, UserService userService) {
        this.staticPageRepository = staticPageRepository;
        this.userService = userService;
    }

    @Override
    public Long save(StaticPageDto staticPageDto) {
        StaticPage staticPage = new StaticPage();
        staticPage.setTitle(staticPageDto.getTitle());
        staticPage.setContent(staticPageDto.getContent());
        staticPage.setSlug(staticPageDto.getSlug());
        staticPage.setDisplayOrder(staticPageDto.getDisplayOrder());
        staticPage.setType(staticPageDto.getType());
        staticPage.setStatus(staticPageDto.getStatus());

        User currentUser = userService.findCurrentUser();
        staticPage.setAuthor(currentUser);

        staticPage.setCreatedAt(LocalDateTime.now());
        staticPage.setModifiedAt(LocalDateTime.now());
        return staticPageRepository.save(staticPage);
    }

    @Override
    public void update(StaticPageDto staticPageDto) {
        StaticPage staticPage = staticPageRepository.findById(staticPageDto.getId());
        staticPage.setTitle(staticPageDto.getTitle());
        staticPage.setContent(staticPageDto.getContent());
        staticPage.setSlug(staticPageDto.getSlug());
        staticPage.setDisplayOrder(staticPageDto.getDisplayOrder());
        staticPage.setType(staticPageDto.getType());
        staticPage.setStatus(staticPageDto.getStatus());

        staticPage.setModifiedAt(LocalDateTime.now());
        staticPageRepository.update(staticPage);
    }

    @Override
    public StaticPageDto findById(Long id) {
        StaticPage staticPage = staticPageRepository.findById(id);
        return pageToPageDto(staticPage);
    }

    @Override
    public StaticPageDto findBySlug(String slug) {
        StaticPage staticPage = staticPageRepository.findBySlug(slug);
        return pageToPageDto(staticPage);
    }

    @Override
    public Page<StaticPageDto> find(Pageable pageable) {
        return staticPageRepository.find(addOrder(pageable));
    }

    @Override
    public Page<StaticPageDto> find(Types.PostStatus pageStatus, Pageable pageable) {
        return staticPageRepository.find(addOrder(pageable), pageStatus);
    }

    @Override
    public void delete(StaticPageDto staticPageDto) {
        StaticPage staticPage = staticPageRepository.findById(staticPageDto.getId());
        staticPageRepository.delete(staticPage);
    }

    @Override
    public void deleteById(Long id) {
        StaticPage staticPage = staticPageRepository.findById(id);
        staticPageRepository.delete(staticPage);
    }

    @Override
    public Long count() {
        return staticPageRepository.count();
    }

    private StaticPageDto pageToPageDto(StaticPage staticPage) {
        StaticPageDto staticPageDto = new StaticPageDto();
        staticPageDto.setId(staticPage.getId());
        staticPageDto.setTitle(staticPage.getTitle());
        staticPageDto.setContent(staticPage.getContent());
        staticPageDto.setType(staticPage.getType());
        staticPageDto.setAuthorDisplayName(staticPage.getAuthor().getDisplayName());
        staticPageDto.setSlug(staticPage.getSlug());
        staticPageDto.setDisplayOrder(staticPage.getDisplayOrder());
        staticPageDto.setModifiedAt(staticPage.getModifiedAt());
        staticPageDto.setCreatedAt(staticPage.getCreatedAt());
        staticPageDto.setStatus(staticPage.getStatus());
        return staticPageDto;
    }

    private Pageable addOrder(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "displayOrder");
    }
}
