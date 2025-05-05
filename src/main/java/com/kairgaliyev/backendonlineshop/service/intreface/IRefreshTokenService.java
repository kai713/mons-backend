package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.entity.RefreshTokenEntity;

import java.util.Optional;

public interface IRefreshTokenService {
    RefreshTokenEntity createRefreshToken(String email);

    Optional<RefreshTokenEntity> findByToken(String token);

    boolean validateRefreshToken(RefreshTokenEntity token);

    void invalidateRefreshToken(String tokenValue);
}
