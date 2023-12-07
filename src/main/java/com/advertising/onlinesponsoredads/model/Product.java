package com.advertising.onlinesponsoredads.model;

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
@Table(indexes = @Index(columnList = "serialNumber", name = "idx_product_serial_number"))
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
    @JoinTable(name = "product_category",
            joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
    @ToString.Exclude
    private Set<Category> categories = new HashSet<>();

    public void addCampaign(Campaign campaign) {
        campaigns.add(campaign);
        campaign.getProducts().add(this);
    }
    public void removeCampaign(Campaign campaign) {
        campaigns.remove(campaign);
        campaign.getProducts().remove(this);
    }
    public void addCategory(Category category) {
        categories.add(category);
        category.getProducts().add(this);
    }
    public void removeCategory(Category category) {
        categories.remove(category);
        category.getProducts().remove(this);
    }
}
