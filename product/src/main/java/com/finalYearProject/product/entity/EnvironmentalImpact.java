package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvironmentalImpact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Double carbonFootprintKg;
    @Column(name = "water_usage_l")
    private Double waterUsageL;
    private Double recyclabilityPercent;
    private Double energy;
    private Double wasteGenerated;
}
