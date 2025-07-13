// CouponCode.java
package com.finalYearProject.product.entity;

import com.finalYearProject.product.constant.COUPON_TYPE;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "coupon_codes")
public class CouponCode {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Double maxPrice;

    @Enumerated(EnumType.STRING)
    private COUPON_TYPE couponType;

    private String couponValue;

    /** Site genelindeyse boş kalır, değilse sadece bu markalara uygulanır */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coupon_brand",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id")
    )
    private Set<Brands> applicableBrands = new HashSet<>();

    /** Site genelindeyse boş kalır, değilse sadece bu kategorilere uygulanır */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "coupon_category",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> applicableCategories = new HashSet<>();
    /** Aynı kupon birden çok kullanıcıya atanabilir */
    @ManyToMany(mappedBy = "couponCodes", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();
}
