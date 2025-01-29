package com.kairgaliyev.backendonlineshop.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ProductDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    @Positive(message = "product price must be a positive value")
    @Positive(message = "product price must be a positive value")
    private Double price;
    private Integer stock;
    private Long categoryId;
}
