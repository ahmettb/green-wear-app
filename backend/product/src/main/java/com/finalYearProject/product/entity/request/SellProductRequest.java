package com.finalYearProject.product.entity.request;

import lombok.Data;

@Data
public class SellProductRequest
{

    private Long productId;

    private Integer count;

    private String size;

    private Long userId;
}
