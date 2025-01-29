package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.enums.OrderStatus;
import com.kairgaliyev.backendonlineshop.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusRoleRequest {
    private String role;

    public UserRole getUserRoleEnum() {
        return UserRole.valueOf(role.toUpperCase());
    }

    public OrderStatus getOrderStatusEnum() {
        return OrderStatus.valueOf(role.toUpperCase());
    }
}

