package com.finalYearProject.product.mapper;


import com.finalYearProject.product.entity.Product;
import com.finalYearProject.product.entity.request.CreateProductRequest;
import com.finalYearProject.product.entity.response.ProductResponse;

import java.util.ArrayList;
import java.util.List;

public class ProductMapper {


    public static Product mapToEntity(CreateProductRequest request) {

        Product product = new Product();
//        product.setBrand(request.getBrand());
//        product.setName(request.getName());
//        product.setMaterial(request.getMaterial());
//        product.setSizeXL(request.getSizeXL());
//        product.setSizeL(request.getSizeL());
//        product.setSizeM(request.getSizeM());
//        product.setPrice(request.getPrice());
//        product.setSizeS(request.getSizeS());
        return product;

    }

    public static ProductResponse mapToResponse(Product product) {
        ProductResponse response = new ProductResponse();

        response.setName(product.getName());
        response.setId(product.getId());
        response.setCategory(product.getCategory().getName());
        response.setBrand(product.getBrand().getName());
        response.setCarbonFootprint(product.getEnvironmentalImpact().getCarbonFootprintKg());
        response.setPrice(product.getPrice());
        response.setWaterUsage(product.getEnvironmentalImpact().getWaterUsageL());
        response.setPrice(product.getPrice());
        response.setWasteGenerated(product.getEnvironmentalImpact().getWasteGenerated());
     //   response.setSizeL(product.getLSizeCount());
     //   response.setSizeM(product.getMSizeCount());
      //  response.setSizeS(product.getSSizeCount());
      //  response.setSizeXL(product.getXlSizeCount());
        response.setStock(product.getStock());
      //  response.setRecyclabilityScore(product.getEnvironmentalImpact().getRecyclabilityPercent());

        return  response;
    }

    public static List<ProductResponse>mapToList(List<Product> productList)
    {

        List<ProductResponse>productResponseList=new ArrayList<>();

        for(Product product: productList)
        {

           productResponseList.add( mapToResponse(product));
        }

        return  productResponseList;

    }
}
