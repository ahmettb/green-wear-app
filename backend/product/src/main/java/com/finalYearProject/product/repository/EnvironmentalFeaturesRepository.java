package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.EnvironmentalImpact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentalFeaturesRepository extends JpaRepository<EnvironmentalImpact,Long> {
}
