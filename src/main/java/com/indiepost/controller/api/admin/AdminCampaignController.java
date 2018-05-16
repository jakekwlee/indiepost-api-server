package com.indiepost.controller.api.admin;

import com.indiepost.dto.stat.CampaignDto;
import com.indiepost.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Objects;

/**
 * Created by jake on 8/10/17.
 */
@RestController
@RequestMapping(value = "/api/admin/campaign", produces = {"application/json; charset=UTF-8"})
public class AdminCampaignController {

    private final CampaignService campaignService;

    @Autowired
    public AdminCampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public Page<CampaignDto> getCampaigns(Pageable pageable) {
        return campaignService.find(pageable);
    }

    @GetMapping(value = "/{id}")
    public CampaignDto getCampaign(@PathVariable Long id) {
        return campaignService.findById(id);
    }

    @PostMapping
    public CampaignDto createCampaign(@Valid CampaignDto campaignDto) {
        return campaignService.save(campaignDto);
    }

    @PutMapping(value = "/{id}")
    public void updateCampaign(@PathVariable Long id, @Valid CampaignDto campaignDto) {
        if (!Objects.equals(id, campaignDto.getId())) {
            throw new ValidationException("REST resource ID and CampaignDto::id are not match.");
        }
        campaignService.update(campaignDto);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteCampaign(@PathVariable Long id) {
        campaignService.deleteById(id);
    }

}
