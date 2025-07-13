package com.finalYearProject.product.entity;

import com.finalYearProject.product.constant.RANK;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class UserRank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private RANK rank;

    private Integer minLimitPoint;

    private Integer maxLimitPoint;


}
