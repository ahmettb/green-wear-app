package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
