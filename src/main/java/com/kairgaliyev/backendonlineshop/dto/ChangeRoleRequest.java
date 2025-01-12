package com.kairgaliyev.backendonlineshop.dto;

import com.kairgaliyev.backendonlineshop.enums.UserRole;

public class ChangeRoleRequest {
    private String role;

    public UserRole getRoleEnum() {
        return UserRole.valueOf(role.toUpperCase());
    }

    // Getters and Setters

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

