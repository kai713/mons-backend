package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.dto.AuthResponse;
import com.kairgaliyev.backendonlineshop.entity.RefreshTokenEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface IRefreshTokenService {
    RefreshTokenEntity createRefreshToken(String email);

    void invalidateRefreshToken(HttpServletRequest request);

    AuthResponse refreshAccessToken(String refreshToken);
}
