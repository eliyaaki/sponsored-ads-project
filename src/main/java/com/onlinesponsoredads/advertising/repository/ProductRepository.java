package com.onlinesponsoredads.advertising.repository;

import com.onlinesponsoredads.advertising.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllBySerialNumberIn(List<String> serialNumbers);

    @Query(value = "SELECT p.* FROM Product p " +
            "JOIN product_campaign cp ON p.id = cp.product_id " +
            "JOIN Campaign c ON cp.campaign_id = c.id " +
            "WHERE c.start_date + INTERVAL '7 days' >= CURRENT_DATE " +
            "ORDER BY c.bid DESC "+
            "LIMIT 1 ", nativeQuery = true)
    Optional<Product> findPromotedProductWithHighestBid();
}
