package com.finalYearProject.product.controller;


import com.finalYearProject.product.entity.request.OrderRequest;
import com.finalYearProject.product.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController extends BaseController {

    private final PaymentService paymentService;

    @PostMapping(PAYMENT_CREATE_ORDER)
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {

        paymentService.createOrder(request);

        return ResponseEntity.ok().body("Ödeme Başarılı");

    }

    @GetMapping(GET_ORDER_LIST)
    public ResponseEntity<?> getOrderListById(@PathVariable("userId")Long userId) {


        return ResponseEntity.ok().body(paymentService.getOrderListByUserId(userId));

    }
    @GetMapping(GET_ORDER_DETAIL)
    public ResponseEntity<?> getOrderDetailById(@PathVariable("orderId")Long orderId) {


        return ResponseEntity.ok().body(paymentService.getOrderDetail(orderId));

    }
}
