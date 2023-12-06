package com.onlinesponsoredads.advertising.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    private double price;
    @Column(unique = true)
    private String serialNumber;

    @ManyToMany
    @JoinTable(name = "product_campaign",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "campaign_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Campaign> campaigns = new HashSet<>();


    @ManyToMany
//    @ManyToMany(cascade = {
//            CascadeType.PERSIST,
//            CascadeType.MERGE
//    }, fetch = FetchType.LAZY)
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Category> categories = new HashSet<>();
}
