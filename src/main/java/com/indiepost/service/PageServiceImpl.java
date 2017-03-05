package com.indiepost.service;

import com.indiepost.dto.PageDto;
import com.indiepost.model.Page;
import com.indiepost.model.User;
import com.indiepost.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by jake on 17. 3. 5.
 */
@Service
@Transactional
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;

    private final UserService userService;

    @Autowired
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

        User currentUser = userService.getCurrentUser();
        page.setAuthor(currentUser);

        Date now = new Date();
        page.setCreatedAt(now);
        page.setModifiedAt(now);
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

        Date now = new Date();
        page.setModifiedAt(now);
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
    public List<PageDto> find(int page, int maxResults, boolean isDesc) {
        return pageRepository.find(getPageable(page, maxResults, isDesc));
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
        return pageDto;
    }

    private Pageable getPageable(int page, int maxResults, boolean isDesc) {
        return isDesc ?
                new PageRequest(page, maxResults, Sort.Direction.DESC, "displayOrder") :
                new PageRequest(page, maxResults, Sort.Direction.ASC, "displayOrder");
    }
}
