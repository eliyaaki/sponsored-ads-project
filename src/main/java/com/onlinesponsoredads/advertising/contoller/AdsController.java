package com.onlinesponsoredads.advertising.contoller;

import com.onlinesponsoredads.advertising.dto.request.CreateCampaignRequest;
import com.onlinesponsoredads.advertising.dto.response.CreateCampaignResponseDTO;
import com.onlinesponsoredads.advertising.dto.response.ServeAdResponseDTO;
import com.onlinesponsoredads.advertising.service.AdsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/ads")
public class AdsController {
    private final AdsService adsService;

    @PostMapping("/createCampaign")
    public ResponseEntity<CreateCampaignResponseDTO> createCampaign(@RequestBody @Valid CreateCampaignRequest request) {
        log.info("createCampaign api got called with: {}", request);
        CreateCampaignResponseDTO campaign = adsService.createCampaign(request);
        log.info("createCampaign finished successfully, with campaign:{}", campaign);
        return new ResponseEntity<>(campaign, HttpStatus.CREATED);
    }

    @GetMapping("/serveAd")
    public ResponseEntity<ServeAdResponseDTO> serveAd(
            @RequestParam
            @Size(min = 2, max = 20, message = "category must have at least 2 letters")
            String category) {
            ServeAdResponseDTO ad = adsService.getAdByCategory(category);
            log.info("serveAd finished successfully, with ad:{}", ad);
            return ResponseEntity.ok(ad);
    }
}