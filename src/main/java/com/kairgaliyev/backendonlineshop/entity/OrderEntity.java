package com.kairgaliyev.backendonlineshop.entity;

import com.kairgaliyev.backendonlineshop.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}
