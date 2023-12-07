package com.onlinesponsoredads.advertising.service;

import com.onlinesponsoredads.advertising.dto.request.CreateCampaignRequest;
import com.onlinesponsoredads.advertising.dto.response.CreateCampaignResponseDTO;
import com.onlinesponsoredads.advertising.dto.response.ServeAdResponseDTO;
import com.onlinesponsoredads.advertising.exception.NotFoundException;
import com.onlinesponsoredads.advertising.model.Campaign;
import com.onlinesponsoredads.advertising.model.Category;
import com.onlinesponsoredads.advertising.model.Product;
import com.onlinesponsoredads.advertising.repository.CampaignRepository;
import com.onlinesponsoredads.advertising.repository.CategoryRepository;
import com.onlinesponsoredads.advertising.repository.ProductRepository;
import com.onlinesponsoredads.advertising.util.ConversionUtil;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CampaignRepository campaignRepository;
    private final ConversionUtil conversionUtil;


    @Transactional
    public CreateCampaignResponseDTO createCampaign(CreateCampaignRequest campaignRequest) {
        log.info("Creating a new campaign...");
        // Fetch products by their IDs
        Set<Product> products = productRepository.findAllBySerialNumberIn(campaignRequest.getProductSerialNumbers());
        if (products.isEmpty()) throw new NotFoundException("Didnt find any Product With the provided serial number");

        log.info("Fetching new campaign matching products: {}", products);
        Campaign campaign = conversionUtil.convertFromCreateCampaignRequest(campaignRequest, products);

        // Save the new campaign and associate products
        saveCampaignAndAssociateProducts(campaign, products);

        log.info("Campaign and associated products created successfully.");
        return conversionUtil.convertToCampaignResponseDTO(campaign);
    }

    @Transactional
    public ServeAdResponseDTO getAdByCategory(String categoryName) {
        log.info("Fetching ad for category: {}", categoryName);

        Category category = categoryRepository.findByNameWithProductsAndCampaigns(categoryName)
                .orElseThrow(() -> new NotFoundException("Category not found"));

        log.info("Pulled the category from the DB: {}", category);

        Set<Product> products = category.getProducts();
        log.info("Found all products associated with the category: {}", products);

        Optional<Product> suitablePromotedProduct = findSuitablePromotedProduct(products);
        return suitablePromotedProduct
                .map(product -> {
                    log.info("Found a suitable ad: {}", product);
                    return conversionUtil.convertToServeAdResponseDTO(product);
                })
                .orElseThrow(() -> new NotFoundException("No suitable promoted product found."));
    }

    private Optional<Product> findSuitablePromotedProduct(Set<Product> products) {
        log.debug("Finding suitable product...");
        if (products.isEmpty()) {
            return productRepository.findPromotedProductWithHighestBid();
        }
        // Find the product with the highest bid from the associated campaign
        return products.stream()
                .filter(product -> !product.getCampaigns().isEmpty())
                .max(Comparator.comparingDouble(this::getMaxBidOfActiveCampaigns));
    }
    private double getMaxBidOfActiveCampaigns(Product product) {
            log.debug("Calculating max bid of active campaigns for product ID: {}", product.getId());
            return product.getCampaigns().stream()
                    .filter(c->isActiveCampaign(c))
                    .mapToDouble(Campaign::getBid)
                    .max()
                    .orElse(0.0);
    }

    private boolean isActiveCampaign(Campaign campaign) {
        try {
            log.debug("Checking if campaign ID {} is active...", campaign.getId());
            if (campaign == null || campaign.getStartDate() == null || !campaign.isActive()) {
                // If the campaign is null or not active, return false
                return false;
            }
            if (campaign.getStartDate().plusDays(7).isBefore(LocalDate.now())) {
                // If 7 days have passed since start date, update isActive to false and return false
                campaign.setActive(false);
                log.info("Campaign ID {} is set to no longer be active.", campaign.getId());
                return false;
            }
            campaign.setActive(true);
            log.debug("Campaign ID {} is set to be active.", campaign.getId());
            // If the campaign is active and 7 days haven't passed, return true
            return true;
        } catch (Exception e) {
            log.error("An error occurred while checking if the campaign is active", e);
            throw new IllegalStateException("An error occurred while checking if the campaign is active", e);
        }
    }

    private void saveCampaignAndAssociateProducts(Campaign campaign, Set<Product> products) {
        try {
            // Create a copy of the products set to avoid ConcurrentModificationException
            Set<Product> productsCopy = new HashSet<>(products);

            // Associate the new campaign with existing products using addCampaign method
            productsCopy.forEach(product -> product.addCampaign(campaign));

            // Save the new campaign
            campaignRepository.save(campaign);
            log.info("Campaign and Products associated with it saved successfully.");
        } catch (PersistenceException e) {
            log.error("Error occurred while saving campaign and associating products: {}", e.getMessage());
            throw new PersistenceException("Failed to save campaign and associate products.", e);
        } catch (Exception e) {
            log.error("Error occurred: {}", e.getMessage());
            throw new RuntimeException("Failed to save campaign and associate products.", e);
        }
    }
}
