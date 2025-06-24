package com.kairgaliyev.backendonlineshop.entity.mapper;

import com.kairgaliyev.backendonlineshop.enums.UserRole;

import java.util.UUID;

/**
 * Класс для генерации UUID, role
 */
public class UserContextMapper {
    public String generateRandomName() {
        return UUID.randomUUID().toString();
    }

    public UserRole getDefaultUnauthorizedRole() {
        return UserRole.UNAUTHORIZED;
    }
}
