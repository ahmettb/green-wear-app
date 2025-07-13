package com.finalYearProject.product.service;

import com.finalYearProject.product.entity.Brands;
import com.finalYearProject.product.entity.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {


    public static Specification<Product> nameLike(String nameLike) {
        return (root, query, builder) -> builder.like(root.get("name"), "%" + nameLike + "%");
    }

    public static Specification<Product> hasDoctorInSpeciality(String speciality) {
        return (root, query, builder) -> {
            Join<Brands,Product> hospitalDoctors = root.join("brands");
            return builder.equal(hospitalDoctors.get("speciality"), speciality);
        };
    }


}
