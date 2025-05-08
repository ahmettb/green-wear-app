package com.finalYearProject.product.service;

import com.finalYearProject.product.entity.Brands;
import com.finalYearProject.product.entity.Category;
import com.finalYearProject.product.entity.CouponCode;
import com.finalYearProject.product.entity.User;
import com.finalYearProject.product.entity.request.CreateCouponRequest;
import com.finalYearProject.product.entity.response.BrandResponse;
import com.finalYearProject.product.entity.response.CategoryResponse;
import com.finalYearProject.product.entity.response.CouponCodeResponse;
import com.finalYearProject.product.repository.BrandRepository;
import com.finalYearProject.product.repository.CategoryRepository;
import com.finalYearProject.product.repository.CouponCodeRepository;
import com.finalYearProject.product.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class CouponService {

    private final CouponCodeRepository couponCodeRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;

    public void createCoupon(CreateCouponRequest request) {

        CouponCode couponCode = new CouponCode();

        couponCode.setCouponType(request.getCouponType());
        couponCode.setCouponValue(request.getCouponValue());
        couponCode.setTitle(request.getTitle());
        couponCode.setMaxPrice(request.getMaxPrice());

        Set<Brands> brandsSet = new HashSet<>();
        Set<Category> categorySet = new HashSet<>();
        List<User> userList = new ArrayList<>();
        request.getApplicableBrands().forEach(brandId ->
        {

            Brands brands = brandRepository.findById(brandId).orElseThrow(() -> new EntityNotFoundException());
            brandsSet.add(brands);

        });

        request.getApplicableCategories().forEach(categoryId ->
        {

            Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new EntityNotFoundException());
            categorySet.add(category);

        });

        request.getUserId().forEach(userId ->
        {

            User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());
            userList.add(user);

        });


        couponCode.setApplicableBrands(brandsSet);
        couponCode.setApplicableCategories(categorySet);
        couponCode.setUsers(userList);

        couponCodeRepository.save(couponCode);


    }

    public List<CouponCodeResponse> getCouponByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException());

        List<CouponCodeResponse> couponCodeResponses = new ArrayList<>();
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        List<BrandResponse> brandResponseList = new ArrayList<>();
        user.getCouponCodes().forEach(couponCode -> {

            BrandResponse brandResponse = new BrandResponse();
            CategoryResponse categoryResponse = new CategoryResponse();
            CouponCodeResponse response = new CouponCodeResponse();

            response.setTitle(couponCode.getTitle());
            response.setMaxPrice(couponCode.getMaxPrice());
            couponCode.getApplicableCategories().stream().toList().forEach(category -> {

                categoryResponse.setId(category.getId());
                categoryResponse.setName(category.getName());
                categoryResponseList.add(categoryResponse);
            });
            couponCode.getApplicableBrands().stream().toList().forEach(brand -> {

                brandResponse.setId(brand.getId());
                brandResponse.setName(brand.getName());
                brandResponseList.add(brandResponse);
            });

            response.setCategoryResponses(categoryResponseList);
            response.setBrandResponses(brandResponseList);
            couponCodeResponses.add(response);

        });


        return couponCodeResponses;
    }
}
