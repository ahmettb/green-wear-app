package com.finalYearProject.product.entity.request;


import lombok.Data;

import java.util.List;

@Data

public class OrderRequest {


    private Long userId;

    private Long couponId;

    private List<OrderItemRequest> orderItemRequestList;

}
