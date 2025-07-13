package com.finalYearProject.product.entity;


import com.finalYearProject.product.constant.Status;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {


    public abstract Long getId();

    public abstract void setId(Long id);

    private Integer status = Status.ACTIVE; //1 aktif, 0 pasif

    public boolean isActive() {
        return this.status == Status.ACTIVE;
    }

    public void setInactive() {
        this.status = Status.PASSIVE;
    }

    public void setActive() {
        this.status = Status.ACTIVE;
    }


    @CreatedDate
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime updateDateTime;

    @CreatedBy
    private Long insertBy;

    @LastModifiedBy
    private Long lastModifiedBy;
}
