package com.finalYearProject.product.repository;

import com.finalYearProject.product.entity.PasswordResetToken;
import com.finalYearProject.product.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    @Query("select p from PasswordResetToken p join fetch p.user where p.passwordResetAuthCode=:token and p.endDate > :endDate and p.status=1")
    Optional<PasswordResetToken> getActiveByTokenAndEndDateAfter(String token, LocalDateTime endDate);

    @Query("select p from PasswordResetToken p join fetch p.user where p.user=:user and p.endDate > :endDate and p.status=1")
    List<PasswordResetToken> getActiveByUserAndEndDateAfter(User user, LocalDateTime endDate);

    @Modifying
    @Query("update PasswordResetToken p set p.status = 0 where p.id=:id")
    void updateStatusPassive(Long id);

    Boolean existsByPasswordResetAuthCodeAndStatusAndEndDateAfter(String passwordResetAuthCode, int status, LocalDateTime endDate);

}