package com.kairgaliyev.backendonlineshop.dto;


import com.kairgaliyev.backendonlineshop.enums.UserRole;
import com.kairgaliyev.backendonlineshop.entity.OrderEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO implements Serializable {

    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private List<OrderEntity> orderEntities = new ArrayList<>();
}
