package com.finalYearProject.product.entity.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data

public class OrderDetailResponse {


    private Long orderId;

    private Date createDate;

    private Double totalAmount;

    private Double couponValue;

    private String couponName;

    private Double netAmount;

    private List<OrderProductResponse> responseList;




}
