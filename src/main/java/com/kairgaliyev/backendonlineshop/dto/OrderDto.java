package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.enums.OrderStatus;

public record OrderDto(Long id, OrderStatus status, Long userId, Long orderId) {
}
