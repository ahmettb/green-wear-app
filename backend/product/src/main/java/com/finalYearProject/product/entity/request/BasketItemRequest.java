package com.finalYearProject.product.entity.request;

import lombok.Data;

@Data
public class BasketItemRequest {

    Long userId;

    Long productId;

    Integer count;

    String size;
}
