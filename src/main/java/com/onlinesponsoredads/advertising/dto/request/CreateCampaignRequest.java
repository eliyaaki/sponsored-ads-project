package com.onlinesponsoredads.advertising.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCampaignRequest {
    @NotEmpty(message = "Name is required")
    @Size(min = 2, max = 20, message = "Name variable must have at least 2 letters")
    private String name;

    @NotNull
    @FutureOrPresent
    private LocalDate startDate;

    @NotEmpty(message = "ProductSerialNumbers is required")
    private List<String> productSerialNumbers;

    @NotNull(message = "Bid is required")
    private Double bid;
}
