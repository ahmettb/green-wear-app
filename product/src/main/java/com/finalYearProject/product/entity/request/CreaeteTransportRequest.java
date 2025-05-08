package com.finalYearProject.product.entity.request;

import com.finalYearProject.product.constant.FUEL_TYPE;
import com.finalYearProject.product.constant.TRANSPORT_METHOD;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreaeteTransportRequest {

    private String shipmentId;

    private String fromLocation;

    private String toLocation;

    private TRANSPORT_METHOD transportType;

    private FUEL_TYPE fuelType;

    private double distanceKm;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String transporterCompany;

    private String vehicleId;

    private int productCount;

    private double totalWeightKg;

}
