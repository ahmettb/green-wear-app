package com.finalYearProject.product.entity.response;

import lombok.Data;

@Data
public class OrderProductResponse {


    private Long productId;

    private String productName;

    private String category;

    private String imageUrl;

    private Double carbon;

    private Double water;

    private Double wasteGenerated;

    private  Double energy;

    private Integer count;

    private Double point;

    private Double price;
}
