package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String password;
    private String email;
    private String username;
    private Double sustainableScore;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_coupon",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    private List<CouponCode> couponCodes = new ArrayList<>();

    /** Bir kullanıcı birden çok quiz oluşturabilir/çözebilir */
    // Renamed to userQuizzes, mapping to the new join entity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuiz> userQuizzes = new ArrayList<>();

    /** Kullanıcının rütbesi */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rank_id")
    private UserRank rank;
}