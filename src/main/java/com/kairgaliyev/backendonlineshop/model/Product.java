package com.kairgaliyev.backendonlineshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Positive(message = "product price must be a positive value")
    private BigDecimal price;

    @Min(value = 0, message = "stock quantity cannot be negative")
    private Integer stockQuantity;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(name = "product_order",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    List<Order> ordersRelatedThisProduct = new ArrayList<>();
}
