package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.enums.UserRole;

public record UserRequest(Long id, String email, String name, String phone, String password, UserRole role) {
}
