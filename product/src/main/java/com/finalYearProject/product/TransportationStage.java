package com.finalYearProject.product;

import com.finalYearProject.product.entity.Product;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class TransportationStage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transportMethod; // Kamyon, gemi, uçak vb.
    private String fromLocation; // Nereden çıktı
    private String toLocation; // Nereye gitti
    private Double transportCarbonFootprint; // Taşıma sırasında oluşan karbon ayak izi

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}