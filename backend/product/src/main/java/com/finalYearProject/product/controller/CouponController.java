package com.finalYearProject.product.controller;


import com.finalYearProject.product.entity.response.CouponCodeResponse;
import com.finalYearProject.product.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CouponController extends BaseController {


    private final CouponService couponService;

    @GetMapping(COUPUN_GET_COUPON)
    public ResponseEntity<List<CouponCodeResponse>> getCouponCode(@PathVariable("userId") Long userId) {


        List<CouponCodeResponse> couponCodeResponse = couponService.getCouponByUserId(userId);

        return new ResponseEntity<>(couponCodeResponse, HttpStatus.OK);
    }
}
