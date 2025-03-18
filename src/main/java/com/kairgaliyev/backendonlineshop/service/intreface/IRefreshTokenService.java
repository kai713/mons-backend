package com.kairgaliyev.backendonlineshop.service.intreface;

import com.kairgaliyev.backendonlineshop.model.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(String email);

    Optional<RefreshToken> findByToken(String token);

    boolean validateRefreshToken(RefreshToken token);
}
