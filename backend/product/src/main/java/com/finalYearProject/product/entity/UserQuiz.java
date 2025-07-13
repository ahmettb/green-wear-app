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
    private Boolean isCompleted = false; // True if the user has completed the quiz

    @Column(name = "is_passed")
    private Boolean isPassed; // True if the user passed the quiz (optional)

    @Column(name = "user_score")
    private Integer userScore = 0; // Score achieved by the user in this quiz

    @Column(name = "attempt_count")
    private Integer attemptCount = 0; // Number of attempts (optional)

    @Column(name = "last_Youtubeed")
    private Integer lastQuestionAnswered = 0; // To track progress within the quiz
}