package com.finalYearProject.product.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class PasswordResetToken extends BaseEntity {

    public static final int EXPIRATION = 60 * 24;

    public static final String ID_GENERATOR = "password_id";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = ID_GENERATOR)
    @SequenceGenerator(name = ID_GENERATOR, sequenceName = ID_GENERATOR + "_seq", allocationSize = 1)
    private Long id;

    private String passwordResetAuthCode;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private LocalDateTime endDate;
}
