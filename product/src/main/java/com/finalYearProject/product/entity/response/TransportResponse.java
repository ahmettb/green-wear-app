package com.finalYearProject.product.entity.response;


import com.finalYearProject.product.constant.FUEL_TYPE;
import com.finalYearProject.product.constant.TRANSPORT_METHOD;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransportResponse {


    private Long id;

    private String shipmentId;
    private String fromLocation;
    private String toLocation;
    private String transportType;


    private String fuelType;

    private double distanceKm;
    private double estimatedCO2Kg;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String transporterCompany;
    private String vehicleId;
    private int productCount;
    private double totalWeightKg;
}
