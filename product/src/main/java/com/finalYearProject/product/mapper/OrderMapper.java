package com.finalYearProject.product.mapper;

import com.finalYearProject.product.entity.OrderItem;
import com.finalYearProject.product.entity.PaymentInfo;
import com.finalYearProject.product.entity.Product;
import com.finalYearProject.product.entity.response.OrderDetailResponse;
import com.finalYearProject.product.entity.response.OrderProductResponse;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    public static List<OrderProductResponse> mapToOrderDetailR(List<OrderItem> orderItemList) {
        {
            List<OrderProductResponse> responseList = new ArrayList<>();

            for (OrderItem item : orderItemList) {
                OrderProductResponse response = new OrderProductResponse();
                response.setCount(item.getQuantity());
                response.setCategory(item.getProduct().getCategory().getName());
                response.setCarbon(item.getProduct().getEnvironmentalImpact().getCarbonFootprintKg());
                response.setProductName(item.getProduct().getName());
                response.setProductId(item.getProduct().getId());
                response.setWater(item.getProduct().getEnvironmentalImpact().getWaterUsageL());
                response.setWasteGenerated(item.getProduct().getEnvironmentalImpact().getWasteGenerated());
                response.setPrice(item.getProduct().getPrice().doubleValue());

                responseList.add(response);
            }


            return responseList;

        }
    }

    public static OrderDetailResponse mapToOrderDetail(PaymentInfo paymentInfo) {

        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();

        orderDetailResponse.setOrderId(paymentInfo.getId());
        orderDetailResponse.setCreateDate(paymentInfo.getCreatedDate());
        orderDetailResponse.setCouponValue(paymentInfo.getCouponCode() != null ? Double.valueOf(paymentInfo.getCouponCode().getCouponValue()) : 0.0);
        orderDetailResponse.setCouponName(paymentInfo.getCouponCode() != null ? paymentInfo.getCouponCode().getTitle() : null);
        orderDetailResponse.setTotalAmount(paymentInfo.getTotalPrice());

        orderDetailResponse.setResponseList(mapToOrderDetailR(paymentInfo.getOrderItems()));


        return  orderDetailResponse;

    }
}
