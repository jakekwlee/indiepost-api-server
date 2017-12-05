package com.indiepost.controller.api.admin;

import com.indiepost.dto.analytics.CampaignDto;
import com.indiepost.service.CampaignService;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Objects;

/**
 * Created by jake on 8/10/17.
 */
@RestController
@RequestMapping(value = "/api/admin/campaign", produces = {"application/json; charset=UTF-8"})
public class AdminCampaignController {

    private final CampaignService campaignService;

    @Inject
    public AdminCampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<CampaignDto> getCampaigns() {
        return campaignService.findAll();
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
