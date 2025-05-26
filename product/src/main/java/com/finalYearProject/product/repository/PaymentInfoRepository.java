package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.PaymentInfo;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo,Long> {


    @Query("SELECT p from PaymentInfo p where p .user.id= :userId order by p.createdDate desc")
        List<PaymentInfo> getOrderDetailByUserId(@Param("userId")Long userId);
}
