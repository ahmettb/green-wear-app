package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Brands {
    @Id
    private Long id;
    private String name;
}