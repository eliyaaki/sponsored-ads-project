package com.advertising.onlinesponsoredads.contoller;

import com.advertising.onlinesponsoredads.dto.request.CreateCampaignRequestDTO;
import com.advertising.onlinesponsoredads.dto.response.CreateCampaignResponseDTO;
import com.advertising.onlinesponsoredads.dto.response.ServeAdResponseDTO;
import com.advertising.onlinesponsoredads.service.AdsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<CreateCampaignResponseDTO> createCampaign(@RequestBody @Valid CreateCampaignRequestDTO request) {
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