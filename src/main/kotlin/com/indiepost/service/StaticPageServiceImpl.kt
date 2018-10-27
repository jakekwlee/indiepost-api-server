package com.indiepost.service

import com.indiepost.dto.StaticPageDto
import com.indiepost.enums.Types
import com.indiepost.exceptions.ResourceNotFoundException
import com.indiepost.model.StaticPage
import com.indiepost.repository.StaticPageRepository
import com.indiepost.utils.DateUtil.localDateTimeToInstant
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * Created by jake on 17. 3. 5.
 */
@Service
@Transactional
class StaticPageServiceImpl @Inject
constructor(private val staticPageRepository: StaticPageRepository, private val userService: UserService) : StaticPageService {

    @CacheEvict(cacheNames = ["static-page::rendered"], key = "#dto.slug")
    override fun save(dto: StaticPageDto): Long {
        val staticPage = StaticPage()
        staticPage.title = dto.title
        staticPage.content = dto.content
        staticPage.slug = dto.slug
        staticPage.displayOrder = dto.displayOrder
        staticPage.type = dto.type
        staticPage.status = dto.status ?: Types.PostStatus.DRAFT

        val currentUser = userService.findCurrentUser()
        staticPage.author = currentUser

        staticPage.createdAt = LocalDateTime.now()
        staticPage.modifiedAt = LocalDateTime.now()
        return staticPageRepository.save(staticPage)
    }

    @Caching(evict = [
        CacheEvict(cacheNames = arrayOf("static-page::rendered"), key = "#dto.slug"),
        CacheEvict(cacheNames = arrayOf("home::rendered"), allEntries = true)])
    override fun update(dto: StaticPageDto) {
        val staticPage = staticPageRepository.findById(dto.id!!)
                ?: throw ResourceNotFoundException("No staticPage with id: " + dto.id)
        staticPage.title = dto.title
        staticPage.content = dto.content
        staticPage.slug = dto.slug
        staticPage.displayOrder = dto.displayOrder
        staticPage.type = dto.type
        dto.status?.let {
            staticPage.status = it
        }
        staticPage.modifiedAt = LocalDateTime.now()
        staticPageRepository.update(staticPage)
    }

    override fun findById(id: Long): StaticPageDto? {
        val staticPage = staticPageRepository.findById(id)
        return if (staticPage != null)
            pageToPageDto(staticPage)
        else null
    }

    override fun findBySlug(slug: String): StaticPageDto {
        val staticPage = staticPageRepository.findBySlug(slug) ?: throw ResourceNotFoundException()
        return pageToPageDto(staticPage)
    }

    override fun find(pageable: Pageable): Page<StaticPageDto> {
        return staticPageRepository.find(addOrder(pageable))
    }

    override fun find(status: Types.PostStatus, pageable: Pageable): Page<StaticPageDto> {
        return staticPageRepository.find(addOrder(pageable), status)
    }

    override fun delete(staticPageDto: StaticPageDto) {
        val id = staticPageDto.id ?: return
        val staticPage = staticPageRepository.findById(id) ?: return
        staticPageRepository.delete(staticPage)
    }

    override fun deleteById(id: Long) {
        val staticPage = staticPageRepository.findById(id) ?: return
        staticPageRepository.delete(staticPage)
    }

    override fun count(): Long {
        return staticPageRepository.count()
    }

    override fun bulkUpdateStatus(ids: List<Long>, status: Types.PostStatus) {
        staticPageRepository.bulkUpdateStatusByIds(ids, status)
    }

    override fun bulkDeleteByIds(ids: List<Long>) {
        staticPageRepository.bulkDeleteByIds(ids)
    }

    override fun emptyTrash() {
        staticPageRepository.bulkDeleteByStatus(Types.PostStatus.TRASH)
    }

    private fun pageToPageDto(staticPage: StaticPage): StaticPageDto {
        val staticPageDto = StaticPageDto()
        staticPageDto.id = staticPage.id
        staticPageDto.title = staticPage.title
        staticPageDto.content = staticPage.content
        staticPageDto.type = staticPage.type
        staticPageDto.authorDisplayName = staticPage.author!!.displayName
        staticPageDto.slug = staticPage.slug
        staticPageDto.displayOrder = staticPage.displayOrder
        staticPageDto.modifiedAt = localDateTimeToInstant(staticPage.modifiedAt!!)
        staticPageDto.createdAt = localDateTimeToInstant(staticPage.createdAt!!)
        staticPageDto.status = staticPage.status
        return staticPageDto
    }

    private fun addOrder(pageable: Pageable): Pageable {
        return PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.Direction.ASC, "displayOrder")
    }
}
