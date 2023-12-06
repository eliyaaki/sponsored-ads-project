package com.onlinesponsoredads.advertising.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(name = "idx_start_date", columnList = "start_date"),
        @Index(name = "idx_bid", columnList = "bid")
})
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private LocalDate startDate;
    private double bid;

    @ManyToMany(mappedBy = "campaigns", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

    private boolean isActive;
}
