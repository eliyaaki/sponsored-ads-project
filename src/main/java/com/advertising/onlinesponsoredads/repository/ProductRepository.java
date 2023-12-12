package com.advertising.onlinesponsoredads.repository;

import com.advertising.onlinesponsoredads.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Set<Product> findAllBySerialNumberIn(List<String> serialNumbers);

    @Query(value = "SELECT p.* FROM Product p " +
            "JOIN product_campaign cp ON p.id = cp.product_id " +
            "JOIN Campaign c ON cp.campaign_id = c.id " +
            "WHERE c.start_date <= CURRENT_DATE AND c.start_date + :numOfActiveDays >= CURRENT_DATE " +
            "ORDER BY c.bid DESC " +
            "LIMIT 1 ", nativeQuery = true)
    Optional<Product> findPromotedProductWithHighestBid(@Param("numOfActiveDays") int numOfActiveDays);
}
