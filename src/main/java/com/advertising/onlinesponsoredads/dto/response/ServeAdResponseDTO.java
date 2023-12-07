package com.advertising.onlinesponsoredads.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServeAdResponseDTO {
    private Long id;
    private String title;
    private List<String> categories;
    private double price;
    private String serialNumber;
}
