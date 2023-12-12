package com.advertising.onlinesponsoredads.service;

import com.advertising.onlinesponsoredads.dto.request.CreateCampaignRequestDTO;
import com.advertising.onlinesponsoredads.dto.response.CreateCampaignResponseDTO;
import com.advertising.onlinesponsoredads.dto.response.ServeAdResponseDTO;
import com.advertising.onlinesponsoredads.util.ConversionUtil;
import com.advertising.onlinesponsoredads.exception.NotFoundException;
import com.advertising.onlinesponsoredads.model.Campaign;
import com.advertising.onlinesponsoredads.model.Category;
import com.advertising.onlinesponsoredads.model.Product;
import com.advertising.onlinesponsoredads.repository.CampaignRepository;
import com.advertising.onlinesponsoredads.repository.CategoryRepository;
import com.advertising.onlinesponsoredads.repository.ProductRepository;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdsService {
    @Value("${numOfActiveDays:7}")
    private int numOfActiveDays;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CampaignRepository campaignRepository;
    private final ConversionUtil conversionUtil;


    @Transactional
    public CreateCampaignResponseDTO createCampaign(CreateCampaignRequestDTO campaignRequest) {
        log.info("Creating a new campaign...");
        // Fetch products by their IDs
        Set<Product> products = findProductsBySerialNumbers(campaignRequest.getProductSerialNumbers());
        if (products.isEmpty()) throw new NotFoundException("Didnt find any Product With the provided serial number");

        log.info("Fetching new campaign matching products: {}", products);
        Campaign campaign = conversionUtil.convertFromCreateCampaignRequest(campaignRequest, products);

        // Save the new campaign and associate products
        saveCampaignAndAssociateProducts(campaign, products);

        log.info("Campaign and associated products created successfully.");
        return conversionUtil.convertToCampaignResponseDTO(campaign);
    }
    private Set<Product> findProductsBySerialNumbers(List<String> serialNumbers) {
        Set<Product> products = productRepository.findAllBySerialNumberIn(serialNumbers);
        if (products.isEmpty()) {
            throw new NotFoundException("Didn't find any Product with the provided serial numbers");
        }
        return products;
    }
    @Transactional
    public ServeAdResponseDTO getAdByCategory(String categoryName) {
        log.info("Fetching ad for category: {}", categoryName);

        Category category = getCategoryByName(categoryName);
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
    private Category getCategoryByName(String categoryName) {
        return categoryRepository.findByNameWithProductsAndCampaigns(categoryName)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
    public Optional<Product> findSuitablePromotedProduct(Set<Product> products) {
        log.debug("Finding suitable product...");
        if (products == null || products.isEmpty()) {
            return productRepository.findPromotedProductWithHighestBid(numOfActiveDays);
        }
        // Find the product with the highest bid from the associated campaign
        return products.stream()
                .filter(product -> !product.getCampaigns().isEmpty())
                .max(Comparator.comparingDouble(this::getMaxBidOfActiveCampaigns));
    }
    public double getMaxBidOfActiveCampaigns(Product product) {
        return Optional.ofNullable(product)
                .map(p -> {
                    log.debug("Calculating max bid of active campaigns for product ID: {}", p.getId());
                    return p.getCampaigns().stream()
                            .filter(this::isActiveCampaign)
                            .mapToDouble(Campaign::getBid)
                            .max()
                            .orElse(0.0);
                })
                .orElse(0.0);
    }

    private boolean isActiveCampaign(Campaign campaign) {
        try {
            log.debug("Checking if campaign ID {} is active...", campaign.getId());
            if (campaign == null || campaign.getStartDate() == null || !campaign.isActive()) {
                // If the campaign is null or not active, return false
                return false;
            }
            if (campaign.getStartDate().plusDays(numOfActiveDays).isBefore(LocalDate.now())) {
                // If numOfActiveDays(by default its 7) have passed since start date, update isActive to false and return false
                campaign.setActive(false);
                log.info("Campaign ID {} is set to no longer be active.", campaign.getId());
                return false;
            }
            campaign.setActive(true);
            log.debug("Campaign ID {} is set to be active.", campaign.getId());
            return true;
        } catch (Exception e) {
            log.error("An error occurred while checking if the campaign is active", e);
            throw new IllegalStateException("An error occurred while checking if the campaign is active", e);
        }
    }

    private void saveCampaignAndAssociateProducts(Campaign campaign, Set<Product> products) {
        try {
            associateCampaignWithProducts(campaign, new HashSet<>(products));
            campaignRepository.save(campaign);
            log.info("Campaign and Products associated with it saved successfully.");
        } catch (PersistenceException e) {
            handlePersistenceException("Failed to save campaign and associate products.", e);
        } catch (Exception e) {
            handleRuntimeException("Failed to save campaign and associate products.", e);
        }
    }

    private void associateCampaignWithProducts(Campaign campaign, Set<Product> products) {
        products.forEach(product -> product.addCampaign(campaign));
    }

    private void handlePersistenceException(String message, PersistenceException e) {
        log.error("Persistence error occurred: {}", e.getMessage());
        throw new PersistenceException(message, e);
    }

    private void handleRuntimeException(String message, Exception e) {
        log.error("Runtime error occurred: {}", e.getMessage());
        throw new RuntimeException(message, e);
    }
}
