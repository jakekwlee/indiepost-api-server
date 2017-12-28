package com.indiepost.service;

import com.indiepost.dto.PageDto;
import com.indiepost.enums.Types;
import com.indiepost.model.Page;
import com.indiepost.model.User;
import com.indiepost.repository.PageRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
@Service
@Transactional
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    private final UserService userService;

    @Inject
    public PageServiceImpl(PageRepository pageRepository, UserService userService) {
        this.pageRepository = pageRepository;
        this.userService = userService;
    }

    @Override
    public Long save(PageDto pageDto) {
        Page page = new Page();
        page.setTitle(pageDto.getTitle());
        page.setContent(pageDto.getContent());
        page.setSlug(pageDto.getSlug());
        page.setDisplayOrder(pageDto.getDisplayOrder());
        page.setType(pageDto.getType());
        page.setStatus(pageDto.getStatus());

        User currentUser = userService.findCurrentUser();
        page.setAuthor(currentUser);

        page.setCreatedAt(LocalDateTime.now());
        page.setModifiedAt(LocalDateTime.now());
        return pageRepository.save(page);
    }

    @Override
    public void update(PageDto pageDto) {
        Page page = pageRepository.findById(pageDto.getId());
        page.setTitle(pageDto.getTitle());
        page.setContent(pageDto.getContent());
        page.setSlug(pageDto.getSlug());
        page.setDisplayOrder(pageDto.getDisplayOrder());
        page.setType(pageDto.getType());
        page.setStatus(pageDto.getStatus());

        page.setModifiedAt(LocalDateTime.now());
        pageRepository.update(page);
    }

    @Override
    public PageDto findById(Long id) {
        Page page = pageRepository.findById(id);
        return pageToPageDto(page);
    }

    @Override
    public PageDto findBySlug(String slug) {
        Page page = pageRepository.findBySlug(slug);
        return pageToPageDto(page);
    }

    @Override
    public List<PageDto> find(int pageNumber, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(pageNumber, maxResults, isDesc);
        List<PageDto> pageList = pageRepository.find(pageable);
        if (pageList.isEmpty()) {
            return new ArrayList<>();
        }
        return pageList;
    }

    @Override
    public List<PageDto> find(Types.PostStatus pageStatus, int pageNumber, int maxResults, boolean isDesc) {
        Pageable pageable = getPageable(pageNumber, maxResults, isDesc);
        List<PageDto> pageList = pageRepository.find(pageable, pageStatus);
        if (pageList.size() == 0) {
            return null;
        }
        return pageList;
    }

    @Override
    public void delete(PageDto pageDto) {
        Page page = pageRepository.findById(pageDto.getId());
        pageRepository.delete(page);
    }

    @Override
    public void deleteById(Long id) {
        Page page = pageRepository.findById(id);
        pageRepository.delete(page);
    }

    @Override
    public Long count() {
        return pageRepository.count();
    }

    private PageDto pageToPageDto(Page page) {
        PageDto pageDto = new PageDto();
        pageDto.setId(page.getId());
        pageDto.setTitle(page.getTitle());
        pageDto.setContent(page.getContent());
        pageDto.setType(page.getType());
        pageDto.setAuthorDisplayName(page.getAuthor().getDisplayName());
        pageDto.setSlug(page.getSlug());
        pageDto.setDisplayOrder(page.getDisplayOrder());
        pageDto.setModifiedAt(page.getModifiedAt());
        pageDto.setCreatedAt(page.getCreatedAt());
        pageDto.setStatus(page.getStatus());
        return pageDto;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "displayOrder") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "displayOrder");
    }
}
