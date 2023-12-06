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
        List<Product> products = productRepository.findAllBySerialNumberIn(campaignRequest.getProductSerialNumbers());
        log.info("Fetching new campaign matching products: {}", products);
        // Create a new campaign
        Campaign campaign = conversionUtil.convertFromCreateCampaignRequest(campaignRequest, products);

        // Save the new campaign and associate products
        saveCampaignAndAssociateProducts(campaign, products);

        log.info("Campaign and associated products created successfully.");

        return conversionUtil.convertToCampaignResponseDTO(campaign);
    }

    @Transactional
    public ServeAdResponseDTO getAdByCategory(String categoryName) {
        log.info("Fetching ad for category: {}", categoryName);
//        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        Optional<Category> categoryOptional = categoryRepository.findByNameWithProductsAndCampaigns(categoryName);

        if (categoryOptional.isPresent()) {
            Category category = categoryOptional.get();

            log.info("Pulled the category from the DB: {}", category);
            // Fetch all products associated with the category
            Set<Product> products = category.getProducts();
            log.info("Found a suitable products for category: {}", products);
//            // Filter products based on additional conditions
            Optional<Product> suitableProduct = findSuitableProduct(products);

            if (!suitableProduct.isEmpty()) {
                log.info("Found a suitable ad: {}", suitableProduct.get());
                // Find the product with the highest bid
                return conversionUtil.convertToServeAdResponseDTO(suitableProduct.get());
            }
        }
        log.warn("Failed to find appropriate ad for category: {}", categoryName);
        throw new NotFoundException("Failed to find appropriate Ad");
    }



    private Optional<Product> findSuitableProduct(Set<Product> products) {
        log.debug("Finding suitable product...");
        if (products.isEmpty()) {
            var PromotedProduct = productRepository.findPromotedProductWithHighestBid();
            log.debug("products isEmpty so we find PromotedProduct: {}", PromotedProduct);
            return PromotedProduct;
        }
        // Find the product with the highest bid from the associated campaign
        Optional<Product> suitableProduct = products.stream()
                .filter(product -> !product.getCampaigns().isEmpty())
                .max(Comparator.comparingDouble(this::getMaxBidOfActiveCampaigns));

        if (suitableProduct.isPresent()) {
            log.debug("Found a suitable product-ad with ID: {}", suitableProduct.get().getId());
        } else {
            log.debug("No suitable product found.");
        }

        return suitableProduct;
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
        log.debug("Checking if campaign ID {} is active...", campaign.getId());
        if (campaign == null || campaign.getStartDate() == null || !campaign.isActive()) {
            // If the campaign is null or not active, return false
            return false;
        }
        if (campaign.getStartDate().plusDays(7).isBefore(LocalDate.now())) {
            // If 7 days have passed since start date, update isActive to false and return false
            campaign.setActive(false);
            log.info("Campaign ID {} is set to no longer be active.", campaign.getId());
            // Optionally, you might want to persist the changes to the database here
            return false;
        }
        campaign.setActive(true);
        log.debug("Campaign ID {} is set to be active.", campaign.getId());
        // If the campaign is active and 7 days haven't passed, return true
        return true;
    }


    private void saveCampaignAndAssociateProducts(Campaign campaign, List<Product> products) {
        try {

            // Associate products with the campaign
            campaign.getProducts().forEach(product -> product.getCampaigns().add(campaign));
            // Save the new campaign
            campaignRepository.save(campaign);
            log.info("Campaign saved successfully.");
//            // Associate products with the campaign
//            products.forEach(product -> product.getCampaigns().add(campaign));
//
//            // Save the modified products list
//            productRepository.saveAll(products);
//            log.info("Products associated with the campaign saved successfully.");
        } catch (Exception e) {
            log.error("Error occurred while saving campaign and associating products: {}", e.getMessage());
            throw new RuntimeException("Failed to save campaign and associate products.", e);
        }
    }
}
