package com.onlinesponsoredads.advertising.util;

import com.onlinesponsoredads.advertising.dto.request.CreateCampaignRequest;
import com.onlinesponsoredads.advertising.dto.response.CreateCampaignResponseDTO;
import com.onlinesponsoredads.advertising.dto.response.ServeAdResponseDTO;
import com.onlinesponsoredads.advertising.model.Campaign;
import com.onlinesponsoredads.advertising.model.Category;
import com.onlinesponsoredads.advertising.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ConversionUtil {

    public Campaign convertFromCreateCampaignRequest(CreateCampaignRequest createCampaignRequest, Set<Product> products) {
        try {
            Campaign campaign = Campaign.builder()
                    .name(createCampaignRequest.getName())
                    .startDate(createCampaignRequest.getStartDate())
                    .isActive(true)
                    .products(products)
                    .bid(createCampaignRequest.getBid())
                    .build();
            return campaign;
        } catch (Exception e) {
            throw new RuntimeException("Error converting from CreateCampaignRequest to Campaign", e);
        }
    }

    public CreateCampaignResponseDTO convertToCampaignResponseDTO(Campaign campaign) {
        try {
            return CreateCampaignResponseDTO.builder()
                    .id(campaign.getId())
                    .name(campaign.getName())
                    .startDate(campaign.getStartDate())
                    .bid(campaign.getBid())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error converting from Campaign to CreateCampaignResponseDTO", e);
        }

    }

    public ServeAdResponseDTO convertToServeAdResponseDTO(Product product) {
        try {
            return ServeAdResponseDTO.builder()
                    .id(product.getId())
                    .title(product.getTitle())
                    .categories(product.getCategories().stream()
                            .map(Category::getName)
                            .collect(Collectors.toList()))
                    .price(product.getPrice())
                    .serialNumber(product.getSerialNumber())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Error converting from Product to ServeAdResponseDTO", e);
        }
    }
}
