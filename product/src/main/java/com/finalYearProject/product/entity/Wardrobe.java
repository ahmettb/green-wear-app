package com.finalYearProject.product.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table
public class Wardrobe {


    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long  id;


    private User user;

    private List<Product> products;

}
