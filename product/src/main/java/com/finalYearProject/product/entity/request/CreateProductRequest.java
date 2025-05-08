package com.finalYearProject.product.entity.request;


import lombok.Data;

@Data
public class CreateProductRequest {

    private Long categoryId;
    private String name;
    private String brand;
    private String material;
    private Double waterUsage;
    private Double carbonFootprint;
    private String qrCode;
    private Integer sizeS=0;
    private Double price;
    private  Integer sizeM=0;
    private  Integer sizeL=0;
    private Integer sizeXL=0;
    private String factory;
    private String factoryName;
    private String productionMethod;
    private Boolean usesRenewableEnergy;
    private Double productionCarbonFootprint;
    private Double energyUsage;
    private  Double recyclabilityScore;
    private Double wasteGenerated;

}
