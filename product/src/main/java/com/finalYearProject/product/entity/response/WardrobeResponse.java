package com.finalYearProject.product.entity.response;


import com.finalYearProject.product.entity.UserRank;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class WardrobeResponse {


    private Long userId;

    private String rankName;

    private Double totalCo2;

    private Double totalWaterUsage;

    private Double totalEnergyUsage;

    private Double totalWaste;

    private Double averageRecyclabilityScore;

    private List<ProductResponse> productResponses;

    Map<String,Integer> materialList=new HashMap<>();

}
