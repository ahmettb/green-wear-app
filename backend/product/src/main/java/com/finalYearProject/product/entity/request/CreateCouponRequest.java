package com.finalYearProject.product.entity.request;

import com.finalYearProject.product.constant.COUPON_TYPE;
import com.finalYearProject.product.entity.Brands;
import com.finalYearProject.product.entity.Category;
import com.finalYearProject.product.entity.User;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
public class CreateCouponRequest {


    private String title;
    private Double maxPrice;

    @Enumerated(EnumType.STRING)
    private COUPON_TYPE couponType;

    private String couponValue;


    private List<Long> applicableBrands = new ArrayList<>();


    private List<Long> applicableCategories = new ArrayList<>();

    private List<Long> userId;

}
