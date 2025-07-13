package com.finalYearProject.product.entity.request;

import com.finalYearProject.product.constant.COUPON_TYPE;
import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class CreateQuizRequest {

    private List<QuestionRequest> questions;

    private List<Long>userId;

    private String title;
    private String description;
    private Boolean status;
    private Long couponId;
    private Integer minPoint;
}
