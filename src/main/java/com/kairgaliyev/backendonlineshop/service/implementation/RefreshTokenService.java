package com.kairgaliyev.backendonlineshop.service.implementation;

import com.kairgaliyev.backendonlineshop.entity.RefreshTokenEntity;
import com.kairgaliyev.backendonlineshop.entity.UserEntity;
import com.kairgaliyev.backendonlineshop.repository.RefreshTokenRepository;
import com.kairgaliyev.backendonlineshop.repository.UserRepository;
import com.kairgaliyev.backendonlineshop.service.intreface.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements IRefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public RefreshTokenEntity createRefreshToken(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow();

        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setExpiryDate(Instant.now().plus(5, ChronoUnit.MINUTES));

        return refreshTokenRepository.save(refreshTokenEntity);
    }

    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public boolean validateRefreshToken(RefreshTokenEntity token) {
        return token.getExpiryDate().isAfter(Instant.now());
    }

    public void invalidateRefreshToken(String tokenValue) {
        refreshTokenRepository.deleteByToken(tokenValue);
    }
}

