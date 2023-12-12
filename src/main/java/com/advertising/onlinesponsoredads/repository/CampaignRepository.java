package com.advertising.onlinesponsoredads.repository;

import com.advertising.onlinesponsoredads.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {

@Query(value = "SELECT c.* FROM Campaign c " +
        "WHERE c.start_date <= CURRENT_DATE AND c.start_date + :numOfActiveDays >= CURRENT_DATE " +
        "ORDER BY c.bid DESC "+
        "LIMIT 1 ", nativeQuery = true)
    Campaign findActiveCampaignsWithHighestBid(@Param("numOfActiveDays") int numOfActiveDays);
    Optional<Campaign> findByBid(double bid);

}