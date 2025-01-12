package com.kairgaliyev.backendonlineshop.model;

import com.kairgaliyev.backendonlineshop.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToMany
    @JoinTable(name = "product_order",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    List<Product> productsInOrder = new ArrayList<>();

}
