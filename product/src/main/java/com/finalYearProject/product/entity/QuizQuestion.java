// QuizQuestion.java
package com.finalYearProject.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Soru metni */
    @Column(name = "question_text", nullable = false)
    private String questionText;

    /** Bu sorunun puanı */
    private Integer point;

    /** Hangi quize ait? */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id", nullable = false)
    @JsonBackReference             // “geri” taraf, JSON’a yazılmaz
    private Quiz quiz;

    /** Bu sorunun şıkları (cevapları) */
    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonManagedReference(value = "question-answers")
    private List<Option> answers = new ArrayList<>();
}
