package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Entity
@Table
@Data
public class Company {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String location;

    private String address;


    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;


}



