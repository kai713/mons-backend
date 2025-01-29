package com.kairgaliyev.backendonlineshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrderItemDTO implements Serializable {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private double productPrice;
}

