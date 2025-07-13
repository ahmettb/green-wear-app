package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.Product;
import com.finalYearProject.product.entity.response.ProductResponse;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {



    @Query("select  p from Product p where p.category.id in( :ids) ")
    List<Product> findByCategory(@Param("ids") List<Long>ids);


}
