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
        // 1. Kullanıcıyı bul
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            System.err.println("Hata: Kullanıcı bulunamadı. userId: " + request.getUserId());
            return "ERROR: User not found";
        }
        User user = userOptional.get();

        // 2. PaymentInfo objesini oluştur ve kullanıcıyı ata
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setUser(user);
        paymentInfo.setCreatedDate(new Date()); // Sipariş oluşturulma tarihini set et

        // 3. Kupon Kodu İşleme
        // Eğer request'te couponId varsa kuponu ara
        if (request.getCouponId() != null) {
            Optional<CouponCode> couponCodeOptional = couponCodeRepository.findById(request.getCouponId());
            if (couponCodeOptional.isPresent()) {
                code = couponCodeOptional.get();
                // Kuponun uygulanabilir kategori/marka kısıtlamaları yoksa doğrudan ata
                // Eğer kısıtlamalar varsa ve bu siparişle eşleşmiyorsa kuponu atama.
                // Daha karmaşık kupon doğrulama mantığı buraya eklenebilir.
                if (code.getApplicableCategories() == null && code.getApplicableBrands() == null) {
                    paymentInfo.setCouponCode(code);
                } else {
                    // Kupon kısıtlamaları var, bu örnekte uygulanmıyor.
                    // Gerçek uygulamada, sipariş kalemlerinin kupon kısıtlamalarına uyup uymadığı kontrol edilmeli.
                    paymentInfo.setCouponCode(null); // Kupon kısıtlamalı olduğu için uygulanmadı
                }
            } else {
                // Kupon ID'si verildi ama kupon bulunamadı, kuponsuz devam et
                paymentInfo.setCouponCode(null);
                System.out.println("Kupon ID'si '" + request.getCouponId() + "' ile kupon bulunamadı. Sipariş kuponsuz oluşturulacak.");
            }
        } else {
            // Request'te couponId yok, kuponsuz devam et
            paymentInfo.setCouponCode(null);
            System.out.println("Kupon ID'si belirtilmedi. Sipariş kuponsuz oluşturulacak.");
        }

        // 4. Sipariş Kalemlerini İşle ve Toplam Tutarı Hesapla
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

            // OrderItem'ı PaymentInfo'ya ekle.
            // PaymentInfo'daki CascadeType.ALL sayesinde OrderItem'lar PaymentInfo ile birlikte kaydedilecektir.
            paymentInfo.addOrderItem(orderItem);

            // Toplam tutarı hesapla (adet ve satın alma fiyatını dikkate alarak)
            totalAmount += orderItem.getPriceAtPurchase() * orderItem.getQuantity();
        }

        // 5. Kupon indirimini uygula (eğer kupon atandıysa ve indirim miktarı varsa)
        if (paymentInfo.getCouponCode() != null && paymentInfo.getCouponCode().getCouponValue() != null) {
            Double discount = Double.valueOf(paymentInfo.getCouponCode().getCouponValue());
            totalAmount -= discount;
            // Toplam tutarın negatif olmamasını sağla
            if (totalAmount < 0) {
                totalAmount = 0.0;
            }
            System.out.println("Kupon indirimi uygulandı. İndirim miktarı: " + discount + " TL. Yeni toplam: " + totalAmount + " TL");
        }

        // 6. Toplam tutarı PaymentInfo'ya ata ve kaydet
        paymentInfo.setTotalPrice(totalAmount);
        paymentInfoRepository.save(paymentInfo); // PaymentInfo ve ilişkili OrderItem'lar kaydedilir

        userService.userRank(user.getId(), totalPoint);

        System.out.println("Sipariş başarıyla oluşturuldu. PaymentInfo ID: " + paymentInfo.getId());
        return "SUCCESS";
    }

}
