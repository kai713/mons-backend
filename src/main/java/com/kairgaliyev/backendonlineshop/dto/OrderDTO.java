package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDTO implements Serializable {

    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime updatedAt;

    private Long userId;

    private List<OrderItemDTO> orderItems;

    private LocalDateTime createdAt;
}
