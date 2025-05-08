package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.Brands;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brands,Long> {
}
