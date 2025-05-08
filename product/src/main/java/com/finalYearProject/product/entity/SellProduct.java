package com.finalYearProject.product.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class SellProduct {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


}
