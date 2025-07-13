package com.finalYearProject.product.entity.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WardrobRequest {

    private Long userId;

    private LocalDate startDate;

    private LocalDate endDate;
}
