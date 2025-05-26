package com.finalYearProject.product.service;


import com.finalYearProject.product.entity.Product;
import com.finalYearProject.product.entity.User;
import com.finalYearProject.product.entity.request.BasketItemRequest;
import com.finalYearProject.product.repository.ProductRepository;
import com.finalYearProject.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {



    private final UserRepository    userRepository;
    private final ProductRepository productRepository;



}
