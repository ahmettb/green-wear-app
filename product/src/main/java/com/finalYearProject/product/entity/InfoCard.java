package com.finalYearProject.product.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class InfoCard {

    private String title;

    private String description;



}
