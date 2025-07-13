package com.finalYearProject.product.entity.response;

import lombok.Data;

@Data
public class QuizStatusResponse {


    private String status;

    private String couponName;


    private Double couponMinValue;

    private Long couponId;

    private Integer point;

    private String couponType;

}
