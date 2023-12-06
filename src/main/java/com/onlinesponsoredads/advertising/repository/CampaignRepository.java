package com.onlinesponsoredads.advertising.repository;

import com.onlinesponsoredads.advertising.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

@Query(value = "SELECT c.* FROM Campaign c " +
        "WHERE c.start_date + INTERVAL '7 days' >= CURRENT_DATE " +
        "ORDER BY c.bid DESC "+
        "LIMIT 1 ", nativeQuery = true)
    Campaign findActiveCampaignsWithHighestBid();
    Optional<Campaign> findByBid(double bid);

}