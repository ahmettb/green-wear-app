package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.Quiz;
import com.finalYearProject.product.entity.User;
import com.finalYearProject.product.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
    List<UserQuiz> findByUserAndQuiz(User user, Quiz quiz);
}
