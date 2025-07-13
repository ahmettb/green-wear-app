// QuizAnswer.java
package com.finalYearProject.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Option {
    @Id @GeneratedValue Long id;
    private String text;
    private boolean correct;

    @JsonBackReference(value = "question-answers")
    @ManyToOne QuizQuestion question;
}