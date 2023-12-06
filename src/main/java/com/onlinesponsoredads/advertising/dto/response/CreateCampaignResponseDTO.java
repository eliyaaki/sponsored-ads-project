package com.onlinesponsoredads.advertising.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCampaignResponseDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private double bid;
}
