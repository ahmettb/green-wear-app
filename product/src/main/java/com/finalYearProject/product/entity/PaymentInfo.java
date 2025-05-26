package com.finalYearProject.product.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



// PaymentInfo Entity'si
@Entity
@Table(name = "payment_info") // Veritabanındaki tablo adını belirt
public class PaymentInfo {

    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'nin otomatik artan olacağını belirtir
    private Long id;

    // Bir PaymentInfo'nun birden fazla OrderItem'ı olabilir (ürün kalemleri)
    // "mappedBy" ilişkili entity'deki (OrderItem) PaymentInfo alanını işaret eder
    // CascadeType.ALL: PaymentInfo silindiğinde OrderItem'lar da silinir
    // orphanRemoval = true: Bir OrderItem PaymentInfo listesinden çıkarıldığında silinir
    @OneToMany(mappedBy = "paymentInfo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>(); // Ürünler yerine OrderItem listesi

    @Column(nullable = false) // total_price sütunu boş olamaz
    private Double totalPrice;

    // Bir PaymentInfo bir kullanıcıya aittir (Many PaymentInfo to One User)
    // fetch = FetchType.LAZY: Kullanıcı bilgisi sadece ihtiyaç duyulduğunda yüklenir
    // JoinColumn: Veritabanında foreign key sütununun adını belirtir
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // user tablosunun ID'sini tutar
    private User user;

    @Temporal(TemporalType.TIMESTAMP) // java.util.Date için zaman damgası olarak saklanacağını belirtir
    @Column(name = "created_date", nullable = false, updatable = false)
    // created_date sütunu boş olamaz ve güncellenemez
    @CreatedDate
    private Date createdDate;

    // Bir PaymentInfo bir kupon kodu kullanabilir (Many PaymentInfo to One CouponCode)
    // nullable = true: Kupon kodu kullanmak zorunlu değil
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_code_id") // coupon_codes tablosunun ID'sini tutar
    private CouponCode couponCode;

    // Getter ve Setter metotları (Lombok kullanmıyorsan manuel eklemelisin)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public CouponCode getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(CouponCode couponCode) {
        this.couponCode = couponCode;
    }

    // Yardımcı metot: OrderItem eklemek için
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setPaymentInfo(this);
    }

    // Yardımcı metot: OrderItem çıkarmak için
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setPaymentInfo(null);
    }

}