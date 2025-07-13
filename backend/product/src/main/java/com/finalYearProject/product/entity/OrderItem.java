package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Bir OrderItem, bir PaymentInfo'ya aittir
    @JoinColumn(name = "payment_info_id", nullable = false) // payment_info tablosunun ID'sini tutar
    private PaymentInfo paymentInfo;

    @ManyToOne(fetch = FetchType.LAZY) // Bir OrderItem, bir Product'a referans verir
    @JoinColumn(name = "product_id", nullable = false) // product tablosunun ID'sini tutar
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // Ürün adedi

    @Column(nullable = false)
    private Double priceAtPurchase; // Satın alındığı andaki ürün fiyatı (fiyat değişebilir)


}
