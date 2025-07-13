package com.finalYearProject.product.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImpactEnviroment {

    private String month; // Örnek: "2025-05"
    private Double carbon;
    private Double water;
    private Double energy;
    private Double waste;

}
