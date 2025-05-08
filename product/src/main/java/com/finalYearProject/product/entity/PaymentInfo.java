package com.finalYearProject.product.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data

public class PaymentInfo
{

    private Long id;


    private List<Product> products;


    private Double totalPrice;

    private User user;

    private Date createdDate;

  //  private Campaign campaign;
}
