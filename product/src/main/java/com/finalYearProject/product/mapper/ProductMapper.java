package com.finalYearProject.product.mapper;


import com.finalYearProject.product.entity.Product;
import com.finalYearProject.product.entity.request.CreateProductRequest;
import com.finalYearProject.product.entity.response.ProductResponse;

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
        response.setSizeL(product.getLSizeCount());
        response.setSizeM(product.getMSizeCount());
        response.setSizeS(product.getSSizeCount());
        response.setSizeXL(product.getXlSizeCount());
        response.setStock(product.getStock());

        return  response;
    }
}
