package com.finalYearProject.product.service;

import com.finalYearProject.product.entity.*;
import com.finalYearProject.product.entity.request.OrderItemRequest;
import com.finalYearProject.product.entity.request.OrderRequest;
import com.finalYearProject.product.entity.response.OrderDetailResponse;
import com.finalYearProject.product.mapper.OrderMapper;
import com.finalYearProject.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentInfoRepository paymentInfoRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponCodeRepository couponCodeRepository;
    private final RankRepository rankRepository;
    private final UserService userService;

    public List<OrderDetailResponse> getOrderListByUserId(Long userId) {

        List<PaymentInfo> paymentInfoList =
                paymentInfoRepository.getOrderDetailByUserId(userId);
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        for (PaymentInfo paymentInfo : paymentInfoList) {
            orderDetailResponseList.add(OrderMapper.mapToOrderDetail(paymentInfo));
        }
        return orderDetailResponseList;

    }

    public OrderDetailResponse getOrderDetail(Long orderId) {


        PaymentInfo paymentInfo = paymentInfoRepository.findById(orderId).get();
        List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
        OrderDetailResponse response = OrderMapper.mapToOrderDetail(paymentInfo);

        return response;

    }


    @Transactional
    public String createOrder(OrderRequest request) throws Exception {

        CouponCode code = new CouponCode();
        Double totalPoint = 0.0;
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            System.err.println("Hata: Kullanıcı bulunamadı. userId: " + request.getUserId());
            return "ERROR: User not found";
        }
        User user = userOptional.get();

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setUser(user);
        paymentInfo.setCreatedDate(new Date()); // Sipariş oluşturulma tarihini set et


        if (request.getCouponId() != null) {
            Optional<CouponCode> couponCodeOptional = couponCodeRepository.findById(request.getCouponId());
            if (couponCodeOptional.isPresent()) {
                code = couponCodeOptional.get();

                if (code.getApplicableCategories() == null && code.getApplicableBrands() == null) {
                    paymentInfo.setCouponCode(code);
                } else {
                    paymentInfo.setCouponCode(null); // Kupon kısıtlamalı olduğu için uygulanmadı
                }
            } else {
                paymentInfo.setCouponCode(null);
                System.out.println("Kupon ID'si '" + request.getCouponId() + "' ile kupon bulunamadı. Sipariş kuponsuz oluşturulacak.");
            }
        } else {
            paymentInfo.setCouponCode(null);
            System.out.println("Kupon ID'si belirtilmedi. Sipariş kuponsuz oluşturulacak.");
        }

        Double totalAmount = 0.0;
        List<OrderItemRequest> requestList = request.getOrderItemRequestList();

        if (requestList == null || requestList.isEmpty()) {
            System.err.println("Hata: Sipariş kalemleri boş olamaz.");
            return "ERROR: Order items cannot be empty";
        }

        for (OrderItemRequest itemRequest : requestList) {
            Optional<Product> productOptional = productRepository.findById(itemRequest.getProductId());
            if (productOptional.isEmpty()) {
                System.err.println("Hata: Ürün bulunamadı. productId: " + itemRequest.getProductId());
                return "ERROR: Product with ID " + itemRequest.getProductId() + " not found";
            }
            Product product = productOptional.get();
            totalPoint+=product.getPoint();

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            Double indirimOran = 0.0;


            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(itemRequest.getBuyPrice());

            paymentInfo.addOrderItem(orderItem);

            totalAmount += orderItem.getPriceAtPurchase() * orderItem.getQuantity();
        }

        if (paymentInfo.getCouponCode() != null && paymentInfo.getCouponCode().getCouponValue() != null) {
            Double discount = Double.valueOf(paymentInfo.getCouponCode().getCouponValue());
            totalAmount -= discount;
            if (totalAmount < 0) {
                totalAmount = 0.0;
            }
            System.out.println("Kupon indirimi uygulandı. İndirim miktarı: " + discount + " TL. Yeni toplam: " + totalAmount + " TL");
        }

        paymentInfo.setTotalPrice(totalAmount);
        paymentInfoRepository.save(paymentInfo);

        userService.userRank(user.getId(), totalPoint);

        System.out.println("Sipariş başarıyla oluşturuldu. PaymentInfo ID: " + paymentInfo.getId());
        return "SUCCESS";
    }

}
