package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.redis.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

    List<AccessToken> findAllByUserId(Long userId);

}