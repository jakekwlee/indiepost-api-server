package com.indiepost.service;

import com.indiepost.dto.StaticPageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.StaticPage;
import com.indiepost.model.User;
import com.indiepost.repository.StaticPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.indiepost.utils.DateUtil.localDateTimeToInstant;

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
    @CacheEvict(cacheNames = "static-page::rendered", key = "#dto.slug")
    public Long save(StaticPageDto dto) {
        StaticPage staticPage = new StaticPage();
        staticPage.setTitle(dto.getTitle());
        staticPage.setContent(dto.getContent());
        staticPage.setSlug(dto.getSlug());
        staticPage.setDisplayOrder(dto.getDisplayOrder());
        staticPage.setType(dto.getType());
        staticPage.setStatus(dto.getStatus());

        User currentUser = userService.findCurrentUser();
        staticPage.setAuthor(currentUser);

        staticPage.setCreatedAt(LocalDateTime.now());
        staticPage.setModifiedAt(LocalDateTime.now());
        return staticPageRepository.save(staticPage);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "static-page::rendered", key = "#dto.slug"),
            @CacheEvict(cacheNames = "home::rendered", allEntries = true)
    })
    public void update(StaticPageDto dto) {
        StaticPage staticPage = staticPageRepository.findById(dto.getId());
        staticPage.setTitle(dto.getTitle());
        staticPage.setContent(dto.getContent());
        staticPage.setSlug(dto.getSlug());
        staticPage.setDisplayOrder(dto.getDisplayOrder());
        staticPage.setType(dto.getType());
        staticPage.setStatus(dto.getStatus());

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

    @Override
    public void bulkUpdateStatus(List<Long> ids, Types.PostStatus status) {
        staticPageRepository.bulkUpdateStatusByIds(ids, status);
    }

    @Override
    public void bulkDeleteByIds(List<Long> ids) {
        staticPageRepository.bulkDeleteByIds(ids);
    }

    @Override
    public void emptyTrash() {
        staticPageRepository.bulkDeleteByStatus(Types.PostStatus.TRASH);
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
        staticPageDto.setModifiedAt(localDateTimeToInstant(staticPage.getModifiedAt()));
        staticPageDto.setCreatedAt(localDateTimeToInstant(staticPage.getCreatedAt()));
        staticPageDto.setStatus(staticPage.getStatus());
        return staticPageDto;
    }

    private Pageable addOrder(Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.ASC, "displayOrder");
    }
}
