package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.UserRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RankRepository extends JpaRepository <UserRank,Long> {

    List<UserRank> findAllByOrderByMinLimitPointAsc();

}
