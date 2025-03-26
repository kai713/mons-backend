package com.kairgaliyev.backendonlineshop.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
}
