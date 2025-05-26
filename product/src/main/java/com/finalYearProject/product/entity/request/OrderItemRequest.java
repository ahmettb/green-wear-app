package com.finalYearProject.product.entity.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequest {

    private Long productId;

    private Double buyPrice;


    private Integer quantity;

    private Long couponId;




}
