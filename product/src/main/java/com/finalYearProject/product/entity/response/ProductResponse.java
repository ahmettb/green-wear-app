package com.finalYearProject.product.entity.response;

import com.finalYearProject.product.entity.EnvironmentalImpact;
import com.finalYearProject.product.entity.Product;
import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {

    private Long id;
    private String category;

    private String name;
    private String brand;
    private String material;
    private String qrCode;
    private Integer sizeS;
    private  Integer sizeM;
    private  Integer sizeL;
    private Integer sizeXL;
    private Integer stock;
    private Double waterUsage;
    private Double carbonFootprint;

    private Double energyUsage;
    private  Double recyclabilityScore;
    private Double wasteGenerated;

    private String factoryName; // Üretildiği fabrika
    private String productionMethod; // Elle mi? Makineyle mi?
    private Boolean usesRenewableEnergy; // Yenilenebilir enerji kullanımı var mı?
    private Double productionCarbonFootprint; // Üretimdeki karbon ayak izi

    private BigDecimal price;



   // public List<TransportResponse> transportResponseList;





}
