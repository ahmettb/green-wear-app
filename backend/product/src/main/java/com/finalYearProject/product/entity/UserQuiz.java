package com.finalYearProject.product.entity;

// UserQuiz.java

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_quizzes")
public class UserQuiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Column(name = "is_passed")
    private Boolean isPassed;
    @Column(name = "user_score")
    private Integer userScore = 0;

    @Column(name = "attempt_count")
    private Integer attemptCount = 0;

    @Column(name = "last_Youtubeed")
    private Integer lastQuestionAnswered = 0;
}