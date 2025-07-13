package com.finalYearProject.product.entity;


import com.finalYearProject.product.constant.MaterialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private Long id;

    private String name;

    @Column(length = 10)
    private String size;

    private Integer stock;

   // @Column(length = 100)
   // private String material;

    private BigDecimal price;

   // @Column(name = "factory_country")
  //  private String factoryCountry;

   // @Column(name = "s_size_count")
   // private Integer sSizeCount;

    //@Column(name = "m_size_count")
   // private Integer mSizeCount;

    //@Column(name = "l_size_count")
    //private Integer lSizeCount;

    private Double point;


    @Enumerated(EnumType.STRING)
    private MaterialType type;


    private String imageUrl;

   // @Column(name = "xl_size_count")
   // private Integer xlSizeCount;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brands brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private EnvironmentalImpact environmentalImpact;

}