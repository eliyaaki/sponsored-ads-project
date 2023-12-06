package com.onlinesponsoredads.advertising.util;

import com.onlinesponsoredads.advertising.dto.request.CreateCampaignRequest;
import com.onlinesponsoredads.advertising.dto.response.CreateCampaignResponseDTO;
import com.onlinesponsoredads.advertising.dto.response.ServeAdResponseDTO;
import com.onlinesponsoredads.advertising.model.Campaign;
import com.onlinesponsoredads.advertising.model.Category;
import com.onlinesponsoredads.advertising.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConversionUtil {


    public Campaign convertFromCreateCampaignRequest(CreateCampaignRequest createCampaignRequest, List<Product> products) {
        Campaign campaign = Campaign.builder()
                .name(createCampaignRequest.getName())
                .startDate(createCampaignRequest.getStartDate())
                .isActive(true)
                .products(products)
                .bid(createCampaignRequest.getBid())
                .build();
        return campaign;
    }

    public CreateCampaignResponseDTO convertToCampaignResponseDTO(Campaign campaign) {
        CreateCampaignResponseDTO responseDTO = new CreateCampaignResponseDTO();
        responseDTO.setId(campaign.getId());
        responseDTO.setName(campaign.getName());
        responseDTO.setStartDate(campaign.getStartDate());
        responseDTO.setBid(campaign.getBid());
        return responseDTO;
    }

    public ServeAdResponseDTO convertToServeAdResponseDTO(Product product) {
        ServeAdResponseDTO responseDTO = new ServeAdResponseDTO();
        responseDTO.setId(product.getId());
        responseDTO.setTitle(product.getTitle());
        responseDTO.setCategories(product.getCategories().stream()
                .map(Category::getName)
                .collect(Collectors.toList()));
        responseDTO.setPrice(product.getPrice());
        responseDTO.setSerialNumber(product.getSerialNumber());
        return responseDTO;
    }
}
