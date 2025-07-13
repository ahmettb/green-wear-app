package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAnswerRepository extends JpaRepository<Option,Long> {
}
