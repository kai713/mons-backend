package com.kairgaliyev.backendonlineshop.dto;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken, String refreshToken) {
}