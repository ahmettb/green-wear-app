package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion,Long> {
}
