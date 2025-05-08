package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz,Long> {
}
