package com.indiepost.controller.admin

import com.indiepost.dto.CreateResponse
import com.indiepost.dto.analytics.CampaignDto
import com.indiepost.dto.analytics.CampaignReport
import com.indiepost.service.CampaignService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.*
import javax.inject.Inject
import javax.validation.Valid
import javax.validation.ValidationException

/**
 * Created by jake on 8/10/17.
 */
@RestController
@RequestMapping(value = ["/admin/campaign"], produces = ["application/json; charset=UTF-8"])
class AdminCampaignController @Inject
constructor(private val campaignService: CampaignService) {

    @GetMapping
    fun getCampaigns(pageable: Pageable): Page<CampaignDto> {
        return campaignService.find(pageable)
    }

    @GetMapping("/{id}")
    fun getCampaign(@PathVariable id: Long): CampaignDto? {
        return campaignService.findById(id)
    }

    @GetMapping("/_report/{id}")
    fun getCampaignReport(@PathVariable id: Long): CampaignReport {
        return campaignService.getReport(id)
    }

    @PostMapping
    fun createCampaign(@Valid @RequestBody campaignDto: CampaignDto): CreateResponse {
        val id = campaignService.save(campaignDto)
        return CreateResponse(id!!)
    }

    @PutMapping("/{id}")
    fun updateCampaign(@PathVariable id: Long, @Valid @RequestBody campaignDto: CampaignDto) {
        if (id != campaignDto.id) {
            throw ValidationException("REST resource ID and CampaignDto::id are not match.")
        }
        campaignService.update(campaignDto)
    }

    @DeleteMapping("/{id}")
    fun deleteCampaign(@PathVariable id: Long) {
        campaignService.deleteById(id)
    }

}
