package com.finalYearProject.product.entity.response;


import com.finalYearProject.product.constant.COUPON_TYPE;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CouponCodeResponse {


    private String title;
    private Double maxPrice;

    private List<CategoryResponse> categoryResponses;
    private List<BrandResponse> brandResponses;








}
