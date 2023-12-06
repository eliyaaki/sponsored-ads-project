package com.onlinesponsoredads.advertising.repository;

import com.onlinesponsoredads.advertising.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String categoryName);
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products p LEFT JOIN FETCH p.campaigns WHERE c.name = :categoryName")
    Optional<Category> findByNameWithProductsAndCampaigns(@Param("categoryName") String categoryName);

}
