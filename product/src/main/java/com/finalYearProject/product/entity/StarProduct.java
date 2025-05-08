package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class StarProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

}
